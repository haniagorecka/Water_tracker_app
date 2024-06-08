package com.example.watertrackerapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {
    private lateinit var DrinkOption: AppCompatSpinner
    private lateinit var DrinkAmount: EditText
    private lateinit var AddButton: Button
    private lateinit var SmallCup: ImageView
    private lateinit var MediumCup: ImageView
    private lateinit var BigCup: ImageView
    private lateinit var Recommended: TextView
    private lateinit var Consumption: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DrinkOption = findViewById(R.id.DrinkOptions)
        DrinkAmount= findViewById(R.id.DrinkAmount)
        AddButton = findViewById(R.id.AddButton)
        SmallCup = findViewById(R.id.SmallCup)
        MediumCup = findViewById(R.id.MediumCup)
        BigCup = findViewById(R.id.BigCup)
        Recommended = findViewById(R.id.Recommended)
        Consumption=findViewById(R.id.Consumption)

        val uID = intent
        val userID = uID.getStringExtra("uID")
        val userAge = uID.getIntExtra("userAge", 0)
        val userWeight = uID.getDoubleExtra("userWeight", 0.0)
        val userHeight = uID.getDoubleExtra("userHeight", 0.0)
        Recommended.text = Recomendation(userWeight)

         //val userID = intent.getStringExtra("userID") ?: "User"
        setContent {
           WaterTrackerAppTheme {
                Surface {
                    if (userID != null) {
                        WaterIntakeScreen(userID, userAge, userWeight, userHeight)
                    }
                }
            }
        }
    }
}

//@Composable
fun WaterIntakeScreen(name: String, userAge: Int, userWeight: Double, userHeight: Double) {
    var waterIntake by remember { mutableStateOf("") }
    var totalWaterIntake by remember { mutableStateOf(0) }
    val database = Firebase.firestore
    val databaseOp = DatabaseOperations(database)
    val coroutineScope = rememberCoroutineScope()
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
fun Recomendation(weight: Double): String {
     return "Rekomendowane spożycie wody: ${(weight * 35.0).toInt()} ml"
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    WaterTrackerAppTheme {
//        WaterIntakeScreen("Hanna", 25, 70.0, 170.0)
//    }
//}