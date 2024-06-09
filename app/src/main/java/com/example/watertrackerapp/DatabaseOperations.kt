package com.example.watertrackerapp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseOperations(private val database: FirebaseFirestore): FireBaseInterface {

    override suspend fun addUser(email: String, user: User) {
        database.collection("Users").document(email).set(user).await()
    }

    override suspend fun getUser(email: String): User? {
        val snapshot = database.collection("Users")
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

    override suspend fun addWaterIntakeForUser(email: String, amount: Int) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        var waterIntake = WaterIntake(amount, currentDate)
        database.collection("Users")
            .document(email)
            .collection("WaterIntake")
            .document(currentDate)
            .set(waterIntake)
            .await()
    }
    override suspend fun getWaterIntakeForUser(email: String): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val snapshot = database.collection("Users")
            .document(email).collection("WaterIntake")
            .document(currentDate).get().await()
        val amount = snapshot.get("amount").toString().toInt()
        return amount
    }

    override suspend fun editWaterIntakeForUser(email: String, updatedWaterIntake: WaterIntake) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        database.collection("Users")
            .document(email)
            .collection("WaterIntake")
            .document(currentDate)
            .set(updatedWaterIntake)
            .await()
    }
}