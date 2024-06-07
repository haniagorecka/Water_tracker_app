package com.example.watertrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.watertrackerapp.ui.theme.WaterTrackerAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uID = intent
        val userID = uID.getStringExtra("uID")
        val userAge = uID.getIntExtra("userAge", 0)
        val userWeight = uID.getDoubleExtra("userWeight", 0.0)
        val userHeight = uID.getDoubleExtra("userHeight", 0.0)
        //val userID = intent.getStringExtra("userID") ?: "User"
        setContent {
            WaterTrackerAppTheme {
                Surface {
                    if (userID != null) {
                       // WaterIntakeScreen(userID, userAge, userWeight, userHeight)
                    }
                }
            }
        }
    }
}

//@Composable
//fun WaterIntakeScreen(name: String, userAge: Int, userWeight: Double, userHeight: Double) {
//    var waterIntake by remember { mutableStateOf("") }
//    var totalWaterIntake by remember { mutableStateOf(0) }
//    val database = Firebase.firestore
//    val databaseOp = DatabaseOperations(database)
//    val coroutineScope = rememberCoroutineScope()
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
//}

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

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    WaterTrackerAppTheme {
//        WaterIntakeScreen("Hanna", 25, 70.0, 170.0)
//    }
//}