package com.example.tesy2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.data.repository.ClothingRepository
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ClothingViewModel : ViewModel() {

    private val repository = ClothingRepository()

    private val _clothingItems = MutableStateFlow<List<ClothingItem>>(emptyList())
    val clothingItems: StateFlow<List<ClothingItem>> = _clothingItems.asStateFlow()

    fun loadClothes(closetId: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getClothesForCloset(closetId)
                _clothingItems.value = result
            } catch (e: Exception) {
                println("❌ Erreur de chargement des vêtements : ${e.message}")
            }
        }
    }
    fun uploadImageToSupabase(imageBytes: ByteArray, fileName: String) {
        viewModelScope.launch {
            try {
                val bucket = supabase.storage.from("picture-clothes")

                // 1. Upload de l'image
                bucket.upload(
                    path = fileName,
                    data = imageBytes,
                    options = {
                        upsert = true
                    }
                )
                // 2. Récupération de l'URL publique
                val publicUrl = bucket.publicUrl(fileName)
                println("📸 URL publique : $publicUrl")

            } catch (e: Exception) {
                println("❌ Upload failed: ${e.message}")
            }
        }
    }

}
