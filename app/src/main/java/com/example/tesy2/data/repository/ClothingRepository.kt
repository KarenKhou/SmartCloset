package com.example.tesy2.data.repository


import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.data.models.ClothingItemPreview
import com.example.tesy2.data.models.ClothingPreview
import com.example.tesy2.data.models.OutfitRecommendationItem
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

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

    suspend fun getSuggestion(closetId: Int, outfitRecommendation: Int): List<ClothingItem> {
        val raw = supabase
            .from("outfitrecommendation_items")
            .select(Columns.raw("clothingitem(name, image_url)")){
                filter {
                    eq("recommendation_id", outfitRecommendation)
                    eq("clothingitem.closet_id", closetId) }
            }

            .decodeList<ClothingPreview>()

        println("🛠 Raw preview list size: ${raw.size}")
        raw.forEach {
            println("→ ${it.clothingitem?.name} | ${it.clothingitem?.image_url}")
        }


        return raw.mapNotNull { preview ->
            preview.clothingitem?.let {
                ClothingItem(
                    closet_id = closetId,
                    name = it.name,
                    category = null,
                    color = null,
                    material = null,
                    season = null,
                    last_worn = null,
                    image_url = it.image_url,
                    style = null
                )
            }
        }
    }






}



