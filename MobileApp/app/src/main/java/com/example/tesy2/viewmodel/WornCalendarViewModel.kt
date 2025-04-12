package com.example.tesy2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.data.models.ClothingUsageStat
import com.example.tesy2.data.models.UsageWithItem
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.util.Locale.filter
import java.util.Objects.isNull
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.tesy2.data.models.ForgottenItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class WornCalendarViewModel : ViewModel() {

    var wornItemsForDate by mutableStateOf<List<UsageWithItem>>(emptyList())
        private set


    private val _topItems = MutableStateFlow<List<ClothingUsageStat>>(emptyList())
    val topItems: StateFlow<List<ClothingUsageStat>> = _topItems


    fun loadItemsForDate(date: String, userId: String) {
        viewModelScope.launch {
            try {
                val result = supabase.from("usage")
                    .select(columns = Columns.list("item_id", "worn_date", "clothingitem(*)")) {
                        filter {
                            eq("worn_date", date)
                            eq("user_id", userId)
                        }
                    }.decodeList<UsageWithItem>()
                wornItemsForDate = result
            } catch (e: Exception) {
                println("Erreur chargement usage : ${e.message}")
            }
        }
    }



    fun getTop3Items(userId: String) {
        viewModelScope.launch {
            try {
                println("🎯 Fetching top 3 items for user: $userId")

                val results = supabase
                    .from("top_3_used_items2")
                    .select {
                        filter { eq("user_id", userId) }
                        order("rank", Order.ASCENDING)
                    }
                    .decodeList<ClothingUsageStat>()

                println("✅ Top 3 fetched: $results")

                _topItems.value = results

            } catch (e: Exception) {
                println("❌ Erreur top 3: ${e.message}")
            }
        }
    }


    var forgottenItems by mutableStateOf<List<ForgottenItem>>(emptyList())
        private set

    fun loadForgottenItems(userId: String) {
        viewModelScope.launch {
            try {
                println("🧠 Fetching forgotten items for $userId")
                val result = supabase.from("forgotten_items")
                    .select {
                        filter {
                            eq("user_id", userId)
                        }
                    }
                    .decodeList<ForgottenItem>()

                forgottenItems = result
                println("✅ Loaded forgotten items: ${result.size}")
            } catch (e: Exception) {
                println("❌ Error loading forgotten items: ${e.message}")
            }
        }
    }


}