package com.example.watertrackerapp

import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView

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
        if(data!=null)
        {
            dateText.text = data.date
        }

        drinkText.text = "drink1\ndrink2\ndrink3\ndrink1\n" +
                "drink2\n" +
                "drink3drink1\n" +
                "drink2\n" +
                "drink3drink1\n" +
                "drink2\n" +
                "drink3drink1\n" +
                "drink2\n" +
                "drink3drink1\n" +
                "drink2\n" +
                "drink3drink1\n" +
                "drink2\n" +
                "drink3\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml"
        amountText.text = "100 ml\n200 ml\n1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml\n" +
                "200 ml\n" +
                "1000 ml"
    }
}