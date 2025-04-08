package com.example.tesy2.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// Définition de l'interface API pour télécharger l'image et détecter les vêtements
interface ApiService {
    @Multipart
    @POST("detectinout")  // Le chemin de l'API que tu utilises pour détecter les vêtements
    suspend fun uploadImage(
        @Part file: MultipartBody.Part  // L'image envoyée en tant que multipart
    ): Response<Map<String, Any>>  // La réponse JSON sous forme de map
}
