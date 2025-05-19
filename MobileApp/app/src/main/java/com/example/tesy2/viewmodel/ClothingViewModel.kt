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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.tesy2.data.models.Closet
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.formUrlEncode
//import com.example.tesy2.data.repository.createUnsafeKtorClient
//import io.ktor.websocket.WebSocketDeflateExtension.Companion.install
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

import io.ktor.http.Parameters
import io.ktor.http.parametersOf
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


val ngrok: String ="https://f772-185-76-176-184.ngrok-free.app/compare"
val ngrokk: String ="https://f772-185-76-176-184.ngrok-free.app/get_recommendations"

class ClothingViewModel : ViewModel() {

    private val repository = ClothingRepository()

    private val _clothingItems = MutableStateFlow<List<ClothingItem>>(emptyList())
    val clothingItems: StateFlow<List<ClothingItem>> = _clothingItems.asStateFlow()


    private val _publicUrl = MutableStateFlow<String?>(null)
    val publicUrl: StateFlow<String?> = _publicUrl
    private val _matchedItem = mutableStateOf<ClothingItem?>(null)
    val matchedItem: State<ClothingItem?> = _matchedItem





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

    fun loadSuggestion(userId: String, recommendationId: Int) {
        println("📡 loadSuggestion CALLED with user=$userId, rec=$recommendationId")
        viewModelScope.launch {
            try {
                val result = repository.getSuggestion(userId, recommendationId)
                _suggestions.value = result
            } catch (e: Exception) {
                println("❌ Erreur de chargement de suggestion : ${e.message}")
            }
        }
    }

    fun getRecommendations() {
        viewModelScope.launch {
            try {
                val client = HttpClient {
                    install(ContentNegotiation) {
                        json(Json {
                            ignoreUnknownKeys = true
                            prettyPrint = true
                        })
                    }
                }

                println("📡 Sending request to /get_recommendations")

                val response: HttpResponse = client.post(ngrokk) {
                    contentType(ContentType.Application.FormUrlEncoded)
                    setBody(
                        listOf(
                            "outfit_type" to "top+bottom",
                            "gender" to "female",
                            "season" to "summer",
                            "occasion" to "casual",
                            "style" to "casual",
                            "randomize" to "true"
                        ).formUrlEncode()
                    )
                }

                println("✅ Reco API success: ${response.status}")
                // 💡 Tu peux relancer ici un .loadSuggestion() avec un ID si besoin

            } catch (e: Exception) {
                println("❌ Reco API failed: ${e.message}")
            }
        }
    }

    fun generateRecommendation(
        userId : String,
        outfitType: String,
        //gender: String,
        season: String,
        occasion: String,
        //style: String,
        randomize: Boolean
    ) {
        viewModelScope.launch {
            try {
                val client = HttpClient {
                    install(ContentNegotiation) {
                        json(Json { ignoreUnknownKeys = true })
                    }
                }

                val response: HttpResponse = client.submitForm(
                    url = ngrokk,
                    formParameters = Parameters.build {
                        append("userid",userId)
                        append("outfit_type", outfitType)
                        //append("gender", gender)
                        append("season", season)
                        append("occasion", occasion)
                        //append("style", style)
                        append("randomize", randomize.toString())
                    }
                )

                val json = response.bodyAsText()
                println("✅ Reco API success: ${response.status.value} → $json")

                // Parse les item_id depuis la réponse
                val jsonObject = Json.parseToJsonElement(json).jsonObject
                val tops = jsonObject["results"]?.jsonObject?.get("tops")?.jsonArray ?: JsonArray(emptyList())
                val bottoms = jsonObject["results"]?.jsonObject?.get("bottoms")?.jsonArray ?: JsonArray(emptyList())
                val dresses = jsonObject["results"]?.jsonObject?.get("dresses")?.jsonArray ?: JsonArray(emptyList())

                val allIds = (tops + bottoms + dresses).mapNotNull {
                    it.jsonObject["item_id"]?.jsonPrimitive?.intOrNull
                }

                println("🆔 Recommended IDs: $allIds")

                // Fetch clothingitems depuis Supabase avec les IDs
                val result = supabase.from("clothingitem").select {
                    filter {
                        isIn("item_id", allIds)
                    }
                }.decodeList<ClothingItem>()

                println("🧥 Loaded items: ${result.map { it.name }}")

                _suggestions.value = result

            } catch (e: Exception) {
                println("❌ Error generating recommendation: ${e.message}")
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
        val userId = supabase.auth.currentUserOrNull()?.id ?: return
        viewModelScope.launch {
            try {
                val client = HttpClient() {
                    //val client = createUnsafeKtorClient()

                    install(ContentNegotiation) {
                        json(Json {
                            ignoreUnknownKeys = true
                            prettyPrint = true
                        })
                    }
                }


                val request = CompareRequest(image_url = imageUrl, userid=userId)
                //ktor tunnel
                println("hi1")
                val response: CompareResponse = client.post(ngrok) {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }.body()
                println("hi2")


                if (response.status == "ok") {
                    println("🟢 Match found: ${response.match_found}")
                    println("🪪 Best match ID: ${response.match_id}")

                    _matchedItem.value =supabase.from("clothingitem").select(){
                        filter{
                            eq("item_id",response.match_id)
                        }
                    }.decodeSingle<ClothingItem>()

                    println("📏 Name: ${(_matchedItem.value)!!.name}")
                    println("📏 Similarity: ${response.similarity}")
                    _matchItemId.value = response.match_id

                    _showConfirmDialog.value = true
                } else {
                    println("⚠️ Server error: ${response.message}")
                }
                //toggleAvailability(response.match_id)


            } catch (e: Exception) {
                println("❌ API Error: ${e.message}")
            }
        }
    }

    private val _matchItemId = MutableStateFlow<Int?>(null)
    val matchItemId: StateFlow<Int?> = _matchItemId

    private val _showConfirmDialog = MutableStateFlow(false)
    val showConfirmDialog: StateFlow<Boolean> = _showConfirmDialog

    fun dismissDialog() {
        _showConfirmDialog.value = false
        _matchItemId.value = null
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
        style: String,
        availability: String
    ) {
        var av = availability.toInt()
        viewModelScope.launch {
            supabase.from("clothingitem").update(
                mapOf(
                    "name" to name,
                    "category" to category,
                    "color" to color,
                    "style" to style,
                    "availability" to availability
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


    var currentUserId by mutableStateOf<String?>(null)
        private set

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        currentUserId = supabase.auth.currentUserOrNull()?.id
    }

}