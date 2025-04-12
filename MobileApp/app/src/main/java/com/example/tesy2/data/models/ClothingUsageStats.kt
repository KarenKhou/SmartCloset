package com.example.tesy2.data.models

import kotlinx.serialization.Serializable


@Serializable
data class ClothingUsageStat(
    val item_id: Int,
    val name: String,
    val image_url: String?,
    val count: Int,
    val rank: Int
)




