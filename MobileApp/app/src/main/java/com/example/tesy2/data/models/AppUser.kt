package com.example.tesy2.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AppUser(
    val user_id: String,
    val name: String,
    val gender: String? = null,
    val job: String? = null,
    val home_location: String? = null,
    val birth_date: String? = null,
    val theme:String
)