package com.example.watertrackerapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                showErrorSnackBar("All login info provided, wait for log in",false)
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
                        lifecycleScope.launch {
                            val user = getUserInfoFromDB()
                            if (user != null && user.data_set) {
                                goToMainActivity(user)
                            }
                            else if (user!=null && !user.data_set)
                              { goToDataActivity(user) }
                            else
                            {
                                goToDataActivity(email)
                            }
                        }
                        finish()

                    } else{
                        showErrorSnackBar(resources.getString(R.string.err_msg_wrong_login),true)
                    }
                }
        }
    }

    private fun goToMainActivity(user: User) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("uID", user.email)
            putExtra("userAge", user.age)
            putExtra("userWeight", user.weight)
            putExtra("userHeight", user.height)
        }
        startActivity(intent)
        finish()
    }
    private fun goToDataActivity(user: User) {
        val intent = Intent(this, UserDataAquire::class.java).apply {
            putExtra("uID", user.email)
            putExtra("userAge", user.age)
            putExtra("userWeight", user.weight)
            putExtra("userHeight", user.height)
            when(user.gender)
            {
                genderChoice.MALE -> putExtra("userGender", "male")
                genderChoice.FEMALE-> putExtra("userGender", "female")
                else -> putExtra("userGender", "male")
            }
        }
        startActivity(intent)
        finish()
    }
    private fun goToDataActivity(email: String) {
        val intent = Intent(this, UserDataAquire::class.java).apply {
            putExtra("uID", email)
        }
        startActivity(intent)
        finish()
    }

    private suspend fun getUserInfoFromDB(): User? {
        val database = Firebase.firestore
        val databaseOp = DatabaseOperations(database)
        val userID = intent.getStringExtra("uID")
        return if (userID != null) {
            withContext(Dispatchers.IO) {
                databaseOp.getUser(userID)
            }
        } else {
            null
        }
    }

}