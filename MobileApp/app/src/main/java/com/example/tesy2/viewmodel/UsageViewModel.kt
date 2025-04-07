package com.example.tesy2.viewmodel



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

import com.example.tesy2.data.models.UsagePreview


import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import com.example.tesy2.data.models.UsageWithItem

import com.example.tesy2.data.supabase.supabase

import io.github.jan.supabase.postgrest.query.Columns






    suspend fun getRecentUsage(userId: String): List<UsagePreview> {
        val raw = supabase
            .from("usage")
            .select(Columns.raw("worn_date, clothingitem(name, image_url)")) {

                filter {
                    eq("user_id", userId)
                }
                order(column = "worn_date", order = Order.DESCENDING)
                limit(count = 4)

            }

            .decodeList<UsagePreview>()


//        return supabase.from("usage")
//            .select(columns = Columns.list("item_id", "worn_date")) {
//                filter {
//                    eq("user_id", userId)
//                }
//                order(column = "worn_date", order = Order.DESCENDING)
//                limit(count = 4)
//            }.decodeList<UsageWithItem>()
        print("fetching 2")
        return raw
    }

