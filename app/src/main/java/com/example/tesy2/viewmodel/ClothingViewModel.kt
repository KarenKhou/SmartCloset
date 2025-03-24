package com.example.tesy2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.data.models.OutfitRecommendationItem
import com.example.tesy2.data.repository.ClothingRepository
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ClothingViewModel : ViewModel() {

    private val repository = ClothingRepository()

    private val _clothingItems = MutableStateFlow<List<ClothingItem>>(emptyList())
    val clothingItems: StateFlow<List<ClothingItem>> = _clothingItems.asStateFlow()


    private val _publicUrl = MutableStateFlow<String?>(null)
    val publicUrl: StateFlow<String?> = _publicUrl

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
    private val _suggestions = MutableStateFlow<List<ClothingItem>>(emptyList())
    val suggestions: StateFlow<List<ClothingItem>> = _suggestions

    fun loadSuggestion(closetId: Int, recommendationId: Int) {
        println("📡 loadSuggestion CALLED with closet=$closetId, rec=$recommendationId")
        viewModelScope.launch {
            try {
                val result = repository.getSuggestion(closetId, recommendationId)
                _suggestions.value = result
            } catch (e: Exception) {
                println("❌ Erreur de chargement de suggestion : ${e.message}")
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
                val url = bucket.publicUrl(fileName)
                _publicUrl.value = url
                println("📸 URL publique : $publicUrl")

            } catch (e: Exception) {
                println("❌ Upload failed: ${e.message}")
            }
        }
    }


    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl

    fun addClothingItem(item: ClothingItem) {
        viewModelScope.launch {
            try {
                supabase.from("clothingitem").insert(item)
                println("✅ Vêtement inséré avec succès")
            } catch (e: Exception) {
                println("❌ Erreur d'insertion : ${e.message}")
            }
        }
    }


}
