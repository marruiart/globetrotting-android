package com.marina.ruiz.globetrotting.data.network.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
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

    fun getDocRef(collectionName: String, id: String): DocumentReference {
        return client.db.collection(collectionName).document(id)
    }

    suspend fun createDocumentWithId(collectionName: String, data: Any, docId: String) {
        client.db.collection(collectionName).document(docId).set(data).await()
    }

    fun logout() {
        client.auth.signOut()
    }

}