package com.example.watertrackerapp

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirebaseClass {
    private val baseInstance = FirebaseFirestore.getInstance()

    fun registerToFirebaseStore(activity: Registration, info: User)
    {
        baseInstance.collection("Users")
            .document(info.email)
            .set(info, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()

            }
            .addOnFailureListener{
                println("User registration fail")

            }
    }

}