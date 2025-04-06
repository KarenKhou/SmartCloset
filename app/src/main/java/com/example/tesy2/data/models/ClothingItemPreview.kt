package com.example.tesy2.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ClothingPreview(
    val clothingitem: ClothingItemPreview? = null
)

@Serializable
data class ClothingItemPreview(
    val name: String,
    val image_url: String? = null
)
