package com.example.watertrackerapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatSpinner
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



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
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun readUserInfo() {
        if (validateInfo()) {
            val database = Firebase.firestore
            val databaseOp = DatabaseOperations(database)

            val userWeight = inputWeight?.text.toString().toDouble()
            val userAge = inputAge?.text.toString().toInt()
            val userHeight = inputHeight?.text.toString().toDouble()
            val userGender = inputGender?.selectedItem.toString()
            val user = FirebaseAuth.getInstance().currentUser
            val displayName = user?.displayName ?: "Unknown User"
            print("$displayName data: weight $userWeight, age $userAge, height $userHeight, gender $userGender")

            val uID = this.intent
            val userID = uID.getStringExtra("uID")
            val user2 = User()
            user2.weight=userWeight
            if (userID != null) {
               user2.email=userID
            }
            user2.age=userAge
            user2.height=userHeight
            user2.gender = when(userGender)
            {
                "Female"-> genderChoice.FEMALE
                "Male" ->genderChoice.MALE
                else -> genderChoice.NOCHOICE
            }
            GlobalScope.launch(Dispatchers.Main) {
                if(userID!=null)
                {
                    val user1 = databaseOp.getUser(userID)
                    if (user1 != null) {
                        user2.email=user1.email
                        user2.name=user1.name
                        user2.isRegistered=user1.isRegistered
                    }

                }


            }
            GlobalScope.launch(Dispatchers.Main)
            {
                databaseOp.editUser(user2.email, user2)
            }
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
