package com.example.tesy2.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils.result
import com.example.tesy2.data.models.Closet
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import androidx.compose.runtime.State

class AddClosetViewModel : ViewModel(){
    fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }

    fun createNewCloset(item : Closet) {
        viewModelScope.launch {
            try {
                supabase.from("closet").insert(item)
                println("✅ closet inséré avec succès")

            } catch (e: Exception) {
                println("❌ Erreur d'insertion : ${e.message}")
            }
        }
    }




}