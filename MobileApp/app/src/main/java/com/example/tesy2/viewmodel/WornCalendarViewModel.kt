package com.example.tesy2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.data.models.UsageWithItem
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch
import java.util.Objects.isNull

class WornCalendarViewModel : ViewModel() {

    var wornItemsForDate by mutableStateOf<List<UsageWithItem>>(emptyList())
        private set

    fun loadItemsForDate(date: String) {
        viewModelScope.launch {
            try {
                val result = supabase.from("usage")
                    .select(columns = Columns.list("item_id", "worn_date", "clothingitem(*)")) {
                        filter {
                            eq("worn_date", date)
                        }
                    }.decodeList<UsageWithItem>()
                wornItemsForDate = result
            } catch (e: Exception) {
                println("Erreur chargement usage : ${e.message}")
            }
        }
    }
//
//    val mostWornItem = mutableStateOf<ClothingItem?>(null)
//    val dominantColor = mutableStateOf<String?>(null)
//    val forgottenItems = mutableStateOf<List<ClothingItem>>(emptyList())
//    val repeatedItems = mutableStateOf<List<Pair<ClothingItem, Int>>>(emptyList())
//
//    fun loadStats(userId: String) {
//        viewModelScope.launch {
//            // Exemples (à adapter à ton schéma Supabase)
//            mostWornItem.value = getMostWornItem(userId)
//            dominantColor.value = getMostFrequentColor(userId)
//            forgottenItems.value = getItemsNotWornSince(userId, days = 30)
//
//        }
//    }
//
//
//    suspend fun getMostWornItem(userId: String): ClothingItem? {
//        val response = supabase
//            .from("usage")
//            .select(columns=Columns.list()){
//                filter{
//                    eq("user_id", userId)
//
//                }order(column = "worn_date", order = Order.DESCENDING)
//                limit(1)
//            }
//        return response.data.firstOrNull()?.clothingitem
//
//    }
//
//    suspend fun getForgottenItems(closetId: String): List<ClothingItem> {
//        val response = supabase.from("clothingitem")
//            .select(
//                columns = Columns.raw("""
//                *,
//                usage (clothingitem_id)
//            """.trimIndent())
//            ) {
//                filter {
//                    eq("closet_id", closetId)
//                    isNull("usage.clothingitem_id")
//                }
//            }
//            .decodeList<ClothingItem>()
//
//        return response
//    }
//
//


}
