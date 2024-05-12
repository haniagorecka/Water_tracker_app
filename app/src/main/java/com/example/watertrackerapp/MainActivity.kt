package com.example.watertrackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.watertrackerapp.ui.theme.WaterTrackerAppTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userID = intent.getStringExtra("userID") ?: "User"
        setContent {
            WaterTrackerAppTheme {
                Surface {
                    WaterIntakeScreen(userID)
                }
            }
        }
    }
}

@Composable
fun WaterIntakeScreen(name: String) {
    var waterIntake by remember { mutableStateOf("") }
    var totalWaterIntake by remember { mutableStateOf(0) }

    Column(modifier = Modifier.padding(16.dp)) {
        Greeting(name = name)
        OutlinedTextField(
            value = waterIntake,
            onValueChange = { waterIntake = it },
            label = { Text("Dodaj spożycie wody (ml)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                totalWaterIntake += waterIntake.toIntOrNull() ?: 0
                waterIntake = ""
            })
        )
        Button(
            onClick = {
                totalWaterIntake += waterIntake.toIntOrNull() ?: 0
                waterIntake = ""
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Dodaj")
        }
        Text("Całkowite spożycie wody: $totalWaterIntake ml", modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WaterTrackerAppTheme {
        WaterIntakeScreen("Hanna")
    }
}
