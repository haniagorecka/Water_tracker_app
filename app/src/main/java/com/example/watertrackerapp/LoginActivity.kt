package com.example.watertrackerapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class LoginActivity :  BaseActivity()
{

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var loginButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicjalizacja pól wejściowych i przycisku logowania
        inputEmail = findViewById(R.id.inputEmailText1)
        inputPassword = findViewById(R.id.inputPasswordText1)
        loginButton = findViewById(R.id.loginButton)

        // Ustawienie nasłuchiwania kliknięć przycisku logowania
        loginButton?.setOnClickListener{
            logInRegisteredUser()
        }

    }

fun goToRegister(view: View) {
    val newActivity = Intent(this, Registration::class.java)
    startActivity(newActivity)
    finish()
}

    private fun validateLoginDetails(): Boolean {

        return when{
            TextUtils.isEmpty(inputEmail?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(inputPassword?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }

            else -> {
                showErrorSnackBar("Valid login info",false)
                true
            }
        }


    }

    private fun logInRegisteredUser(){


        if(validateLoginDetails()){
            val email = inputEmail?.text.toString().trim(){ it<= ' '}
            val password = inputPassword?.text.toString().trim(){ it<= ' '}

            // Logowanie za pomocą FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        showErrorSnackBar("Login successful", false)
                       // goToDataActivity()
                        val newActivity = Intent(this, UserDataAquire::class.java)
                        startActivity(newActivity)
                        finish()

                    } else{
                        showErrorSnackBar(resources.getString(R.string.err_msg_wrong_login),true)
                    }
                }
        }
    }

    fun goToDataActivity(email: String) {
        val database = Firebase.firestore
        val databaseOp = DatabaseOperations(database)

            val uid = email
            val intent = Intent(this, UserDataAquire::class.java)
            intent.putExtra("uID",uid)
            startActivity(intent)


        //val user = FirebaseAuth.getInstance().currentUser;

    }


}