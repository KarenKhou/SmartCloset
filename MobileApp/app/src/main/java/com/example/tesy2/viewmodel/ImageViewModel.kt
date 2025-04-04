package com.example.tesy2.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesy2.data.repository.ImageRepository
import kotlinx.coroutines.launch
import java.io.File

import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.auth.auth

class ImageViewModel : ViewModel() {

    private val imageRepository = ImageRepository()

    // Fonction pour uploader l'image sur le serveur
    fun uploadImage(imageFile: File) {
        viewModelScope.launch {

            Log.d("Supabase", "Session: ${supabase.auth.currentSessionOrNull()}")

            imageRepository.uploadImageToServer(imageFile)
        }
    }
}
