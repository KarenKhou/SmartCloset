package com.example.tesy2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesy2.data.ClothingAvailability
import com.example.tesy2.data.models.ApiResponse
import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.data.models.CompareRequest
import com.example.tesy2.data.models.CompareResponse
import com.example.tesy2.data.models.OutfitRecommendationItem
import com.example.tesy2.data.repository.ClothingRepository
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


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
                println("❌ Erreur de chargement des vêtements! : ${e.message}")
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





    fun uploadImageToSupabase(imageBytes: ByteArray, fileName: String, bucket : String) {
        viewModelScope.launch {
            try {
                val bucket = supabase.storage.from(bucket)

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




    fun sendToBackend(imageUrl: String) {
        viewModelScope.launch {
            try {
                val client = HttpClient() {
                    install(ContentNegotiation) {
                        json(Json {
                            ignoreUnknownKeys = true
                            prettyPrint = true
                        })
                    }
                }

                val request = CompareRequest(image_url = imageUrl)

                val response: CompareResponse = client.post("https://6a1e-94-187-3-125.ngrok-free.app/compare") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }.body()


                if (response.status == "ok") {
                    println("🟢 Match found: ${response.match_found}")
                    println("🪪 Best match ID: ${response.match_id}")
                    println("📏 Similarity: ${response.similarity}")
                } else {
                    println("⚠️ Server error: ${response.message}")
                }
                toggleAvailability(response.match_id)


            } catch (e: Exception) {
                println("❌ API Error: ${e.message}")
            }
        }
    }

    fun toggleAvailability(itemId: Int) {
        viewModelScope.launch {
            try {
                // 🔍 Récupère l'élément pour connaître sa disponibilité actuelle
                val item = supabase.from("clothingitem")
                    .select(columns = Columns.list("availability")) {
                        filter {
                            eq("item_id", itemId)
                        }
                    }
                    .decodeSingle<ClothingAvailability>()

                val newAvailability = if (item.availability == 1) 0 else 1

                // ✏️ Mise à jour de la colonne availability
                val response = supabase.from("clothingitem")
                    .update(mapOf("availability" to newAvailability)) {
                        filter{
                            eq("item_id", itemId)
                        }

                    }


                println("✅ Availability updated to $newAvailability for item $itemId")

            } catch (e: Exception) {
                println("❌ Error updating availability: ${e.message}")
            }
        }
    }





}
