package com.example.tesy2.data.models

import kotlinx.serialization.Serializable

@Serializable
data class OutfitRecommendationItem(
    val recommendation_id: Int?,
    val item_id: Int?,
    val category: String?,
    val clothingitem: ClothingItem?
)
