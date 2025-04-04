package com.example.tesy2.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ClothingItem(
    val item_id: Int? = null,
    val closet_id: Int,
    val name: String,
    val category: String?,
    val color: String? = null,
    val material: String? = null,
    val season: String? = null,
    val last_worn: String? = null, // Ou LocalDate si tu veux être plus strict
    val image_url: String? = null,
    val style: String? = null,
    val availability : Int,
    //val user_id: String? = null

)
