package com.example.tesy2.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesy2.data.repository.ImageRepository
import kotlinx.coroutines.launch
import java.io.File

class ImageViewModel : ViewModel() {

    private val imageRepository = ImageRepository()

    // Fonction pour uploader l'image sur le serveur
    fun uploadImage(imageFile: File) {
        viewModelScope.launch {
            imageRepository.uploadImageToServer(imageFile)
        }
    }
}
