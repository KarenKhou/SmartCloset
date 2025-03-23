package com.example.tesy2.data.repository



import com.example.tesy2.data.models.AppUser
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    suspend fun signUp(email: String, password: String, user: AppUser): Boolean {
        println("📤 Appel de signUp() and authREpo")
        return try {
            val result = supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val uid = supabase.auth.currentUserOrNull()?.id ?: return false

            supabase.from("User").insert(user.copy(user_id = uid))
            println("📥 Insertion dans la table User faite !")
            true
        } catch (e: Exception) {
            false
        }
    }
}
