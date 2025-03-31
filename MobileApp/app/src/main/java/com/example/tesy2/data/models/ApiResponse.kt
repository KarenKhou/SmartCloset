package com.example.tesy2.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val result: String // adapte en fonction de ce que ton API renvoie
)
