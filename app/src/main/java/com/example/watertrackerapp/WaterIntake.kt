package com.example.watertrackerapp

data class WaterIntake(
    val amount: Int,  // ilość wypitej wody w ml
    val date: String, // data w formacie "yyyy-MM-dd"
    val rec: Int //rekomendowana ilosc spozycia wody
)