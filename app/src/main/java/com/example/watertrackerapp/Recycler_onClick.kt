package com.example.watertrackerapp

import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class Recycler_onClick : BaseActivity() {
    lateinit var dateText: TextView
    lateinit var scrollView: ScrollView
    lateinit var drinkText: TextView
    lateinit var amountText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_on_click)
        dateText = findViewById(R.id.dateText)
        scrollView = findViewById(R.id.scrollView2)
        drinkText = findViewById(R.id.drinksText)
        amountText = findViewById(R.id.amountText)
        val data = intent.getParcelableExtra<RecycleViewData>("data")
        if (data != null) {
            dateText.text = data.date
        }

        val database = Firebase.firestore
        val databaseOp = DatabaseOperations(database)
        val userID = data?.email
        var drinks: String = ""
        var amounts: String = ""
        lifecycleScope.launch {

            if (userID != null) {
                val snapshot = database.collection("Users")
                    .document(userID).collection("WaterIntake")
                    .document(data.date)
                    .collection("Drinks")
                    .get().await()

                snapshot.forEach {

                    val drink = it.toObject(DrinkIntake::class.java)
                    if (drink != null) {
                        drinks += drink.name
                        drinks += "\n"
                        amounts += drink.amount
                        amounts += " ml\n"
                    } else {
                        throw Exception("Exception!!!!!!")
                    }
                }
            }

            drinkText.text = drinks
            amountText.text = amounts
        }
    }
}
