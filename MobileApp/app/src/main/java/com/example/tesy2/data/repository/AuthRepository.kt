package com.example.tesy2.data.repository



import android.content.Context
import com.example.tesy2.data.models.AppUser
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.OAuthProvider
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

            println(e)
            false
        }
    }



    suspend fun updateUserProfile(user: AppUser): Boolean {
        println("📤 Appel de updateUserProfile() dans AuthRepository")
        return try {
            supabase.from("User")
                .update(
                    mapOf(
                        "gender" to user.gender,
                        "job" to user.job,
                        "home_location" to user.home_location,
                        "birth_date" to user.birth_date
                    )
                ) {
                    filter {
                        eq("user_id", user.user_id)
                    }
                }

            println("✅ Profil utilisateur mis à jour avec succès !")
            true
        } catch (e: Exception) {
            println("❌ Erreur lors de la mise à jour du profil: ${e.message}")
            false
        }
    }





    suspend fun signIn(email: String, password: String): Boolean {
        println("📤 Appel de signIn() dans AuthRepository")
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val user = supabase.auth.currentUserOrNull()
            println("✅ Utilisateur connecté : ${user?.id}")
            user != null
        } catch (e: Exception) {
            println("❌ Erreur de connexion : ${e.message}")
            false
        }
    }

    }
