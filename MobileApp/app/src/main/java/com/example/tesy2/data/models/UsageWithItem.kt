package com.example.tesy2.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UsageWithItem(
    val item_id: Int,
    val worn_date: String, // tu peux parser en LocalDate si tu préfères


    val clothingitem: ClothingItem


)
