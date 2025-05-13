package com.example.tesy2.data.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.storage.Storage

val supabase = createSupabaseClient(
    supabaseUrl = "https://rnjccfpgdpzkoptzvcgr.supabase.co",
    supabaseKey = "key"

) {
    install(Postgrest)
    install(Auth)
    install(Storage)

}
