package com.marina.ruiz.globetrotting.data.network.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseService @Inject constructor(val client: FirebaseClient) {

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

    suspend fun updateDocument(collectionName: String, data: HashMap<String, Any>, docId: String) {
        client.db.collection(collectionName).document(docId).update(data).await()
    }

    fun logout() {
        client.auth.signOut()
    }

}