package com.example.watertrackerapp

interface FireBaseInterface {

    suspend fun addUser(email: String, user: User)
    suspend fun editUser(mail: String, updatedUser: User)
    suspend fun getUser(email: String): User?
    suspend fun deleteUser(email: String)
    suspend fun addWaterIntakeForUser(email: String, amount: Int, rec: Int)
    suspend fun getWaterIntakeForUser(email: String): Int
    suspend fun editWaterIntakeForUser(email: String, updatedWaterIntake: WaterIntake)
}