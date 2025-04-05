package com.example.tesy2.data.models


import kotlinx.serialization.Serializable

@Serializable
data class Closet(
    val closet_id : Int? = null, //genere par supabase
    val closet_name: String,
    val size: String? = null,
    val capacity: Int? = null,
    val user_id: String
)