package com.example.tesy2.viewmodel

import android.util.Log
import android.widget.Toast
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
import com.example.tesy2.ui.screens.ClothingScreen
import io.github.jan.supabase.auth.auth
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import androidx.compose.runtime.State
import com.example.tesy2.data.models.Closet
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


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
                Log.d("Supabase", "Session: ${supabase.auth.currentSessionOrNull()}")
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
                viewModelScope.launch {
                    showToast("Upload terminé !")
                }



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
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    suspend fun showToast(message: String) {
        _toastMessage.emit(message)
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
                //ktor tunnel
                val response: CompareResponse = client.post("https://fe49-94-187-2-31.ngrok-free.app/compare") {
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

    fun getClothingItemById(itemId: String): Flow<ClothingItem?> {
        val idInt = itemId.toIntOrNull() ?: return flowOf(null) // en cas d'erreur de parsing
        return clothingItems.map { list -> list.find { it.item_id == idInt } }
    }


    fun updateClothingItem(
        itemId: String,
        name: String,
        category: String,
        color: String,
        style: String
    ) {
        viewModelScope.launch {
            supabase.from("clothingitem").update(
                mapOf(
                    "name" to name,
                    "category" to category,
                    "color" to color,
                    "style" to style
                )){filter {
                        eq("item_id", itemId)
                    }
                }
        }

        }


    private val _selectedItem = mutableStateOf<ClothingItem?>(null)
    val selectedItem: State<ClothingItem?> = _selectedItem

    fun fetchClothingItemByIdFromSupabase(itemId: String) {
        viewModelScope.launch {
            val response = supabase
                .from("clothingitem").select(){
                        filter{
                            eq("item_id", itemId)
                        }
                    }.decodeSingle<ClothingItem>()


            try {
                _selectedItem.value = response

            } catch (e : Exception) {
                println("❌ error: ${e}")
            }
        }
    }



    fun deleteClothingItem(itemId: String) {
        viewModelScope.launch {
            try {
                val response = supabase
                    .from("clothingitem")
                    .delete(){
                        filter{
                            eq("item_id", itemId.toInt())
                        }
                    }


                    println("✅ Item supprimé")


            } catch (e: Exception) {
                println("❌ Exception while deleting item: $e")
            }
        }
    }
    private val _closets = mutableStateOf<List<Closet>>(emptyList())
    val closets: State<List<Closet>> = _closets

    fun loadUserClosets() {
        val userId = supabase.auth.currentUserOrNull()?.id ?: return
        viewModelScope.launch {
            try {
                val result = supabase.from("closet")
                    .select()
                    {filter{
                        eq("user_id", userId)
                    }}
                    .decodeList<Closet>()

                _closets.value = result
            } catch (e: Exception) {
                println("❌ Erreur chargement closets: ${e.message}")
            }
        }
    }

}







