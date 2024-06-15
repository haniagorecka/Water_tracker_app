package com.example.watertrackerapp

import com.google.firebase.firestore.PropertyName

data class DrinkIntake (@get: PropertyName("name") @set: PropertyName("name") var name: String = "",
                        @get: PropertyName("amount") @set: PropertyName("amount") var amount: Int = 0)