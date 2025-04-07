package com.example.tesy2.viewmodel



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import com.example.tesy2.data.models.UsageWithItem
import io.github.jan.supabase.postgrest.query.Columns





    suspend fun getRecentUsage(supabase: SupabaseClient, userId: String): List<UsageWithItem> {
        return supabase.from("usage")
            .select(columns = Columns.list("item_id", "worn_date")) {
                filter {
                    eq("user_id", userId)
                }
                order(column = "worn_date", order = Order.DESCENDING)
                limit(count = 4)
            }.decodeList<UsageWithItem>()
        print("fetching 2")
    }

