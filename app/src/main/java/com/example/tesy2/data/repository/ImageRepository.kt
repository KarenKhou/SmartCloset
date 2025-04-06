package com.example.tesy2.data.repository
import android.util.Log
import com.example.tesy2.api.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class ImageRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.2.247:8000/")  // Remplace par l'IP de ton PC
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun uploadImageToServer(imageFile: File) {
        // Utilisation de la méthode moderne pour créer un RequestBody
        val requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), imageFile)

        // Créer le MultipartBody
        val part = MultipartBody.Part.createFormData("file", imageFile.name, requestBody)

        try {
            val response = apiService.uploadImage(part)
            if (response.isSuccessful) {
                // Traiter les résultats de la détection des vêtements
                val clothesDetected = response.body()?.get("clothesinout")
                Log.d("Detection", "Vêtements détectés : $clothesDetected")
            } else {
                Log.e("Erreur", "Échec de l'upload de l'image : ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Erreur", "Erreur d'upload : ${e.message}")
        }
    }
}
