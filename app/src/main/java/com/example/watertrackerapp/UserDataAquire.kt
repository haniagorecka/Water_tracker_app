package com.example.watertrackerapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.google.firebase.auth.FirebaseAuth


class UserDataAquire :  BaseActivity() {
    private var inputGender: AppCompatSpinner? = null
    private var inputWeight: EditText? = null
    private var inputHeight: EditText? = null
    private var inputAge: EditText? = null
    private var saveData: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdata)

        inputGender = findViewById(R.id.inputGenderText)
        inputWeight = findViewById(R.id.inputWeightNumber)
        inputHeight = findViewById(R.id.inputHeightNumber)
        inputAge = findViewById(R.id.inputAgeNumber)
        saveData = findViewById(R.id.saveButton)

        // Ustawienie nasłuchiwania kliknięć przycisku logowania
        saveData?.setOnClickListener{
            readUserInfo()
        }
    }

    fun goToMain(view: View) {
        val newActivity = Intent(this, MainActivity::class.java)
        startActivity(newActivity)
        finish()
    }

    /**
     * Metoda walidująca wprowadzone dane logowania.
     * @return True, jeśli dane są poprawne, w przeciwnym razie False.
     */
    private fun validateLoginDetails(): Boolean {

        return when{
            TextUtils.isEmpty(inputWeight?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(inputAge?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            TextUtils.isEmpty(inputHeight?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            else -> {
                showErrorSnackBar("All data aquired",false)
                true
            }
        }


    }

    private fun readUserInfo() {

       val userWeight = inputWeight?.text.toString().toDouble()
       val userAge = inputAge?.text.toString().toInt()
       val userHeight = inputHeight?.text.toString().toDouble()
     //  val userGender = inputGender?. .toString()
       val user = FirebaseAuth.getInstance().currentUser;
       val uid = user?.email.toString()
       print("$uid data: weight $userWeight, age $userAge, height $userHeight, weight $userWeight")
       goToMainActivity()

    }

  fun goToMainActivity() {
        val user = FirebaseAuth.getInstance().currentUser;
        val uid = user?.email.toString()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("uID",uid)
        startActivity(intent)
    }


}