package com.example.watertrackerapp

import com.google.firebase.firestore.PropertyName

enum class genderChoice
{MALE, FEMALE, NOCHOICE}
data class User (
    @get: PropertyName("name") @set: PropertyName("name") var name: String = "",
    @get: PropertyName("email") @set: PropertyName("email") var email: String = "",
    @get: PropertyName("isRegistered") @set: PropertyName("isRegistered") var isRegistered: Boolean = false,
    @get: PropertyName("age") @set: PropertyName("age") var age: Int= 0,
    @get: PropertyName("weight") @set: PropertyName("weight") var weight: Double = 0.0,
    @get: PropertyName("gender") @set: PropertyName("gender") var gender: genderChoice = genderChoice.NOCHOICE
    )



