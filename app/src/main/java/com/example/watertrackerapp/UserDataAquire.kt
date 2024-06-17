package com.example.watertrackerapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserDataAquire : BaseActivity() {
    private lateinit var inputGender: AppCompatSpinner
    private lateinit var inputWeight: EditText
    private lateinit var inputHeight: EditText
    private lateinit var inputAge: EditText
    private lateinit var saveData: Button
    private lateinit var skip: Button
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdata)
        val uID = intent
        val userID = uID.getStringExtra("uID")
        val database = Firebase.firestore
        val databaseOp = DatabaseOperations(database)
        inputGender = findViewById(R.id.inputGenderText)
        inputWeight = findViewById(R.id.inputWeightNumber)
        inputHeight = findViewById(R.id.inputHeightNumber)
        inputAge = findViewById(R.id.inputAgeNumber)
        saveData = findViewById(R.id.saveButton)
        skip = findViewById(R.id.skipButton)

        // Ustawienia spinnera jako dropdown listy
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.array_name))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        inputGender.adapter = adapter

        saveData.setOnClickListener {
            lifecycleScope.launch {
                val user = getUserInfoFromDB()
                if (user==null)
                {
                    readUserInfo()
                }
                else
                {
                    readUserInfo(user)
                }

            }
        }
        skip.setOnClickListener {
            lifecycleScope.launch {
                val user = getUserInfoFromDB()
                if(user!=null&&user.data_set)
                {
                    goToMainActivity(user)
                }
                else
                {
                    showErrorSnackBar(resources.getString(R.string.err_msg_no_data), true)
                }

            }
        }

        lifecycleScope.launch {
            val user = getUserInfoFromDB()
            if (user!=null&&user.data_set)
                {
                    inputAge.hint = "Wiek: "+user.age.toString()
                    inputHeight.hint = "Wzrost: "+user.height.toString()+" cm"
                    inputWeight.hint ="Waga: "+user.weight.toString()+" kg"
                    when(user.gender)
                    {
                        genderChoice.FEMALE -> {
                            inputGender.setSelection(1)
                        }
                        genderChoice.MALE -> {
                            inputGender.setSelection(0)
                        }
                        else ->
                        {
                            inputGender.setSelection(0)
                        }
                    }
                }
            }

    }

    private fun validateInfo(): Boolean {
        return when {
            TextUtils.isEmpty(inputWeight.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_weight), true)
                false
            }
            TextUtils.isEmpty(inputAge.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_age), true)
                false
            }
            TextUtils.isEmpty(inputHeight.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_height), true)
                false
            }
            else -> {
                showErrorSnackBar("All data acquired", false)
                true
            }
        }
    }

    private suspend fun readUserInfo() {
        if (validateInfo()) {
            val database = Firebase.firestore
            val databaseOp = DatabaseOperations(database)

            val userWeight = inputWeight.text.toString().toDouble()
            val userAge = inputAge.text.toString().toInt()
            val userHeight = inputHeight.text.toString().toDouble()
            val userGender = inputGender.selectedItem.toString()
            val user = FirebaseAuth.getInstance().currentUser
            val displayName = user?.displayName ?: "Unknown User"
            //to usuwa imie

            val newUser = User(
                email = user?.email ?: "",
                name = displayName,
                age = userAge,
                weight = userWeight,
                height = userHeight,
                gender = when (userGender) {
                    "Female" -> genderChoice.FEMALE
                    "Male" -> genderChoice.MALE
                    else -> genderChoice.NOCHOICE
                },
                isRegistered = true,
                data_set = true
            )

            withContext(Dispatchers.IO) {
                databaseOp.editUser(newUser.email, newUser)
            }
            goToMainActivity(newUser)
        }
    }

    private suspend fun readUserInfo(user1: User) {

        if (user1.data_set||validateInfo()) {
            val database = Firebase.firestore
            val databaseOp = DatabaseOperations(database)
            val userWeight: Double
            val userHeight: Double
            val userAge: Int
            if(inputWeight.text.isNotBlank())
            {
                userWeight = inputWeight.text.toString().toDouble()
            }
            else
            {
                userWeight = user1.weight
            }
            if(inputHeight.text.isNotBlank())
            {
                userHeight = inputHeight.text.toString().toDouble()
            }
            else
            {
                userHeight = user1.height
            }
            if(inputAge.text.isNotBlank())
            {
                userAge = inputAge.text.toString().toInt()
            }
            else
            {
                userAge = user1.age
            }
            val userGender = inputGender.selectedItem.toString()
            val user = FirebaseAuth.getInstance().currentUser
            val displayName = user?.displayName ?: "Unknown User"
            //to usuwa imie

            val newUser = User(
                email = user?.email ?: "",
                name = displayName,
                age = userAge,
                weight = userWeight,
                height = userHeight,
                gender = when (userGender) {
                    "Female" -> genderChoice.FEMALE
                    "Male" -> genderChoice.MALE
                    else -> genderChoice.NOCHOICE
                },
                isRegistered = true,
                data_set = true
            )

            withContext(Dispatchers.IO) {
                databaseOp.editUser(newUser.email, newUser)
            }
            goToMainActivity(newUser)
        }
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

    private fun goToMainActivity(user: User) {
        val intent = Intent(this, MainActivity::class.java).apply {
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
}
