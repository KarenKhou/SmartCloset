package com.example.tesy2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesy2.data.models.UsageWithItem
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch

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
}
