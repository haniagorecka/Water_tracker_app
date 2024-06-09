package com.example.watertrackerapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity() {
    private lateinit var DrinkOption: Spinner
    private lateinit var DrinkAmount: EditText
    private lateinit var AddButton: Button
    private lateinit var SmallCup: ImageView
    private lateinit var MediumCup: ImageView
    private lateinit var BigCup: ImageView
    private lateinit var Recommended: TextView
    private lateinit var Consumption: TextView
    private lateinit var Progress: ProgressBar
    private lateinit var GoToStats: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DrinkOption = findViewById(R.id.DrinkOptions)
        DrinkAmount = findViewById(R.id.DrinkAmount)
        AddButton = findViewById(R.id.AddButton)
        SmallCup = findViewById(R.id.SmallCup)
        MediumCup = findViewById(R.id.MediumCup)
        BigCup = findViewById(R.id.BigCup)
        Recommended = findViewById(R.id.Recommended)
        Consumption = findViewById(R.id.Consumption)
        Progress = findViewById(R.id.progressBar)
        GoToStats= findViewById(R.id.buttonStat)

        val database = Firebase.firestore
        val databaseOp = DatabaseOperations(database)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.array_drink)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        DrinkOption.adapter = adapter
        val uID = intent
        val userID = uID.getStringExtra("uID")
        val userAge = uID.getIntExtra("userAge", 0)
        val userWeight = uID.getDoubleExtra("userWeight", 0.0)
        val userHeight = uID.getDoubleExtra("userHeight", 0.0)
        Recommended.text = Recomendation(userWeight).toString()
        Progress.max = Recomendation(userWeight).toInt()
        var totalWaterIntake: Int = 0
        ObjectAnimator.ofInt(Progress, "progress", totalWaterIntake).setDuration(2000).start()
        lifecycleScope.launch {
            if (userID != null) //potencjalnie "try catch"
            {
                try {
                    totalWaterIntake = databaseOp.getWaterIntakeForUser(userID).toInt()
                } catch (e: Exception) {
                    databaseOp.addWaterIntakeForUser(userID, 0)
                }
                Consumption.text = totalWaterIntake.toString()
                if(totalWaterIntake<=Recomendation(userWeight))
                {
                    Progress.progress = totalWaterIntake.toInt()
                }
                else
                {
                    Progress.progress = Progress.max
                }
            }
        }
        AddButton.setOnClickListener()
        {
           val waterIntake = DrinkAmount.text.toString().toInt()
            lifecycleScope.launch {
                if (userID != null)
                {
                    var totalWaterIntake = databaseOp.getWaterIntakeForUser(userID).toInt()
                    totalWaterIntake += waterIntake
                    Consumption.text = totalWaterIntake.toString()
                    databaseOp.addWaterIntakeForUser(userID, totalWaterIntake)
                    if(totalWaterIntake<=Recomendation(userWeight))
                    {
                        Progress.progress = totalWaterIntake.toInt()
                    }
                    else
                    {
                        Progress.progress = Progress.max
                    }
                }
             }
        }

        GoToStats.setOnClickListener()
        {
            lifecycleScope.launch {
                val user = getUserInfoFromDB()
                if (user != null && user.data_set) {
                    goToStatsActivity(user)
                }
            }
        }

        //val userID = intent.getStringExtra("userID") ?: "User"
//         setContent {
//         WaterTrackerAppTheme {
//           Surface {
//        if (userID != null) {
//            WaterIntakeScreen(userID, userAge, userWeight, userHeight)
//              }
//              }
//              }
//              }


    }
//    fun WaterModification(name: String)
//    {
//
//        var waterIntake
//        var totalWaterIntake by remember { mutableStateOf(0) }
//        val database = Firebase.firestore
//        val databaseOp = DatabaseOperations(database)
//        val coroutineScope = rememberCoroutineScope()
//
//
//    }


    @Composable
    fun WaterIntakeScreen(name: String, userAge: Int, userWeight: Double, userHeight: Double) {
        var waterIntake by remember { mutableStateOf("") }
        var totalWaterIntake by remember { mutableStateOf(0) }
        val database = Firebase.firestore
        val databaseOp = DatabaseOperations(database)
        val coroutineScope = rememberCoroutineScope()
        Consumption.text = totalWaterIntake.toString()
        AddButton.setOnClickListener()
        {
            waterIntake = DrinkAmount.text.toString()
            totalWaterIntake += waterIntake.toInt()
            //coroutineScope.launch {
              //     databaseOp.addWaterIntakeForUser(name, totalWaterIntake.toDouble())
           // }
        }


//    Column(modifier = Modifier
//        .padding(16.dp)
//        .fillMaxWidth()
//        .fillMaxHeight()) {
//        Greeting(name = "$name, ")
//        OutlinedTextField(
//            value = waterIntake,
//            onValueChange = { waterIntake = it },
//            label = { Text("Dodaj spożycie wody (ml)") },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//            keyboardActions = KeyboardActions(onDone = {
//                totalWaterIntake += waterIntake.toIntOrNull() ?: 0
//                waterIntake = ""
//            }),
//            modifier = Modifier.fillMaxWidth() // Zastosowanie fillMaxWidth, aby pole tekstowe miało szerokość kolumny
//        )
//        Button(
//            onClick = {
//                totalWaterIntake += waterIntake.toIntOrNull() ?: 0
//                waterIntake = ""
//                coroutineScope.launch {
//                    databaseOp.addWaterIntakeForUser(name, totalWaterIntake.toDouble())
//                }
//            },
//            modifier = Modifier
//                .padding(top = 8.dp)
//                .fillMaxWidth() // Zastosowanie fillMaxWidth, aby przycisk miał szerokość kolumny
//        ) {
//            Text("Dodaj")
//        }
//        Text("Całkowite spożycie wody: $totalWaterIntake ml", modifier = Modifier.padding(top = 8.dp))
//        Recomendation(userWeight, modifier = Modifier.padding(top = 8.dp))
//    }
        }

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}


        @Composable
        fun Recomendation(weight: Double, modifier: Modifier = Modifier) {
            Text(
                text = "Rekomendowane spożycie wody: ${(weight * 35.0).toInt()} ml",
                modifier = modifier
            )
        }

        fun Recomendation(weight: Double): Int{
            return (weight * 35).toInt()
        }

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    WaterTrackerAppTheme {
//        WaterIntakeScreen("Hanna", 25, 70.0, 170.0)
//    }
//}
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
private fun goToStatsActivity(user: User) {
    val intent = Intent(this, RecycleViewActivity::class.java).apply {
        putExtra("uID", user.email)
        putExtra("userAge", user.age)
        putExtra("userWeight", user.weight)
        putExtra("userHeight", user.height)
    }
    startActivity(intent)
    finish()
}
    }
