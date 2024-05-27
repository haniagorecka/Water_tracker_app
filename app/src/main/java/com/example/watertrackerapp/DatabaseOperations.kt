package com.example.watertrackerapp
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await


class DatabaseOperations(private val database: FirebaseFirestore): FireBaseInterface {

    override suspend fun addUser(email: String, user: User) {
        database.collection("Users").document(email).set(user).await()
    }

    override suspend fun getUser(email: String): User? {
        val snapshot = FirebaseFirestore.getInstance().collection("Users")
            .whereEqualTo(FieldPath.documentId(), email)
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.toObject(User::class.java)
    }

    override suspend fun editUser(mail: String, updatedUser: User) {
        database.collection("Users").document(mail).set(updatedUser).await()
    }

    override suspend fun deleteUser(email: String) {
        database.collection("Users").document(email).delete().await()
    }
}