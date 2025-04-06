package com.example.tesy2.data.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.storage.Storage

val supabase = createSupabaseClient(
    supabaseUrl = "https://rnjccfpgdpzkoptzvcgr.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJuamNjZnBnZHB6a29wdHp2Y2dyIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MjU3OTAxMiwiZXhwIjoyMDU4MTU1MDEyfQ.-pmxBnHBuJa9Q9ugBbtj9zojuFBY17BJ7J_atcEqCWc"

) {
    install(Postgrest)
    install(Auth)
    install(Storage)

}
