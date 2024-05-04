package com.marina.ruiz.globetrotting.data.network.firebase

import android.content.Context
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.marina.ruiz.globetrotting.core.compressImage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseService @Inject constructor(
    @ApplicationContext private val context: Context, val client: FirebaseClient
) {

    companion object {
        private const val TAG = "GLOB_DEBUG FIREBASE_SERVICE"
    }

    suspend fun getDocument(collectionName: String, id: String): DocumentSnapshot? {
        return try {
            val document = getDocRef(collectionName, id).get().await()
            if (document.exists()) {
                document
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getCollection(collectionName: String): Task<QuerySnapshot>? {
        return try {
            client.db.collection(collectionName).get()
        } catch (e: Exception) {
            null
        }
    }

    fun getDocRef(collectionName: String, id: String): DocumentReference {
        return client.db.collection(collectionName).document(id)
    }

    fun getCollectionRef(collectionName: String): CollectionReference {
        return client.db.collection(collectionName)
    }

    suspend fun createDocumentWithId(collectionName: String, data: Any, docId: String) {
        client.db.collection(collectionName).document(docId).set(data).await()
    }

    suspend fun createDocument(collectionName: String, data: Any) {
        client.db.collection(collectionName).document().set(data).await()
    }

    suspend fun updateDocument(collectionName: String, data: HashMap<String, Any>, docId: String) {
        client.db.collection(collectionName).document(docId).update(data).await()
    }

    fun logout() {
        client.auth.signOut()
    }

    fun uploadFile(uid: String, file: Uri, callback: StorageFileListeners) {
        val storageRef = client.storage.reference
        val fileRef = storageRef.child(uid)
        val data = file.compressImage(context, 30)
        val uploadTask = if (data != null) {
            fileRef.putBytes(data)
        } else {
            fileRef.putFile(file)
        }

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { ex ->
                    throw ex
                }
            }
            fileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                callback.onUploadSuccess(downloadUri)
            } else {
                callback.onUploadFailed(Exception("Error on removing file."))
            }
        }
    }

    fun removeFile(uid: String, callback: StorageFileListeners) {
        val storageRef = client.storage.reference
        val profileRef = storageRef.child(uid)
        profileRef.delete().addOnSuccessListener { _ ->
            callback.onUploadSuccess(null)
        }.addOnFailureListener {
            callback.onUploadFailed(Exception("Error on removing file."))
        }
    }

}