package com.example.tesy2.data.models

data class MyWeatherResponse(
    val temperature: Double,
    val season: String,
    val description: String,
    val kind: String
)
