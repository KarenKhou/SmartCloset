package com.example.tesy2.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ForgottenItem(
    val item_id: Int,
    val name: String,
    val category: String?,
    val color: String?,
    val material: String?,
    val season: String?,
    val image_url: String?,
    val style: String?,
    val availability: Int,
    val user_id: String
)
