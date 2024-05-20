package com.example.watertrackerapp

interface FireBaseInterface {

    suspend fun addUser()
    suspend fun editUser()
    suspend fun getUser()
    suspend fun deleteUser()

}