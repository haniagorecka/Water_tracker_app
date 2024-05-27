package com.example.watertrackerapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Registration: BaseActivity ()
{
    private var registerButton: Button? = null
    private var inputEmail: EditText? = null
    private var inputName: EditText? = null
    private var inputPassword: EditText? = null
    private var inputRepPass: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.registrationButton)
        inputEmail = findViewById(R.id.inputEmailText)
        inputName = findViewById(R.id.inputNameText)
        inputPassword = findViewById(R.id.inputPasswordText)
        inputRepPass = findViewById(R.id.inputPasswordText2)

        registerButton?.setOnClickListener {
            register()
       }
    }

    fun validateUserInfo(): Boolean
    {
        return when{
        TextUtils.isEmpty(inputEmail?.text.toString().trim{ it <= ' '}) -> {
        showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
        false
    }
        TextUtils.isEmpty(inputName?.text.toString().trim{ it <= ' '}) -> {
        showErrorSnackBar(resources.getString(R.string.err_msg_enter_name),true)
        false
    }
        TextUtils.isEmpty(inputPassword?.text.toString().trim{ it <= ' '}) -> {
        showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
        false
    }
        TextUtils.isEmpty(inputRepPass?.text.toString().trim{ it <= ' '}) -> {
        showErrorSnackBar(resources.getString(R.string.err_msg_enter_reppassword),true)
        false
    }
        inputPassword?.text.toString().trim {it <= ' '} != inputRepPass?.text.toString().trim{it <= ' '} -> {
        showErrorSnackBar(resources.getString(R.string.err_msg_password_mismatch),true)
        false
    }
        else -> true
    }
    }
    private fun register(){
        if (validateUserInfo()){
            val login: String = inputEmail?.text.toString().trim() {it <= ' '}
            val password: String = inputPassword?.text.toString().trim() {it <= ' '}
            val name: String = inputName?.text.toString().trim() {it <= ' '}

            val database = Firebase.firestore
            val databaseOp = DatabaseOperations(database)
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(login,password).addOnCompleteListener(
                OnCompleteListener <AuthResult> { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        showErrorSnackBar(
                            "Successfull registration! Your id is: ${firebaseUser.uid}",
                            false
                        )

                        if (login.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()) {

                            val newUser = User(name, login, true)
                            GlobalScope.launch(Dispatchers.Main) {

                                databaseOp.addUser(login, newUser)
                            }

                            val user = User(
                                name,
                                login,
                                true
                            )

                            FirebaseClass().registerToFirebaseStore(this@Registration, user)

                            FirebaseAuth.getInstance().signOut()
                            val newActivity = Intent(this, LoginActivity::class.java)
                            startActivity(newActivity)
                            finish()

                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                }
                    )


        }
    }


    fun goToLogin(view: View) {
        val newActivity = Intent(this, LoginActivity::class.java)
        startActivity(newActivity)
        finish()
    }


    fun  userRegistrationSuccess(){
        Toast.makeText(this@Registration, resources.getString(R.string.register_success_message), Toast.LENGTH_LONG).show()

    }

}