package com.example.tesy2.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CompareResponse(
    val status: String,
    val match_id: Int,
    val similarity: Float? = null,
    val match_found: Boolean,
    val message: String? = null
)


@Serializable
data class CompareRequest(
    val image_url: String,
    val userid : String,
    val threshold: Float = 0.3f
)

