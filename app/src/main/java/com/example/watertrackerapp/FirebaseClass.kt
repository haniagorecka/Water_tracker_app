package com.example.watertrackerapp

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.watertrackerapp.Registration
import com.google.firebase.firestore.SetOptions

class FirebaseClass {
    private val baseInstance = FirebaseFirestore.getInstance()

    fun registerToFirebaseStore(activity: Registration, info: User)
    {
        baseInstance.collection("users")
            .document(info.id)
            .set(info, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()

            }
            .addOnFailureListener{
                println("User registration fail")

            }
    }

}