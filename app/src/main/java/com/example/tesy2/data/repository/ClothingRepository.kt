package com.example.tesy2.data.repository


import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.postgrest.from

//https://supabase.com/docs/reference/kotlin/eq

class ClothingRepository {
    suspend fun getClothesForCloset(closetId: Int): List<ClothingItem> {
        return supabase.from("clothingitem")
            .select {
                filter {
                    eq("closet_id", closetId)
                }
            }
            .decodeList<ClothingItem>()
    }
}



