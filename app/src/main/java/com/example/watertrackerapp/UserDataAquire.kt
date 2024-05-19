package com.example.watertrackerapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserDataAquire : BaseActivity() {
    private var inputGender: AppCompatSpinner? = null
    private var inputWeight: EditText? = null
    private var inputHeight: EditText? = null
    private var inputAge: EditText? = null
    private var saveData: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdata)

        inputGender = findViewById(R.id.inputGenderText)
        // Ustawienia spinnera jako dropdown listy
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.array_name))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputGender?.adapter = adapter

        inputWeight = findViewById(R.id.inputWeightNumber)
        inputHeight = findViewById(R.id.inputHeightNumber)
        inputAge = findViewById(R.id.inputAgeNumber)
        saveData = findViewById(R.id.saveButton)

        saveData?.setOnClickListener {
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
    private fun validateInfo(): Boolean {
        return when {
            TextUtils.isEmpty(inputWeight?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_weight), true)
                false
            }
            TextUtils.isEmpty(inputAge?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_age), true)
                false
            }
            TextUtils.isEmpty(inputHeight?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_height), true)
                false
            }
            else -> {
                showErrorSnackBar("All data acquired", false)
                true
            }
        }
    }

    private fun readUserInfo() {
        if (validateInfo()) {
            val userWeight = inputWeight?.text.toString().toDouble()
            val userAge = inputAge?.text.toString().toInt()
            val userHeight = inputHeight?.text.toString().toDouble()
            val userGender = inputGender?.selectedItem.toString()
            val user = FirebaseAuth.getInstance().currentUser
            val displayName = user?.displayName ?: "Unknown User"
            print("$displayName data: weight $userWeight, age $userAge, height $userHeight, gender $userGender")
            goToMainActivity(userAge, userWeight, userHeight, displayName)
        }
    }

    fun goToMainActivity(userAge: Int, userWeight: Double, userHeight: Double, displayName: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("uID", displayName)
        intent.putExtra("userAge", userAge)
        intent.putExtra("userWeight", userWeight)
        intent.putExtra("userHeight", userHeight)
        startActivity(intent)
    }
}
