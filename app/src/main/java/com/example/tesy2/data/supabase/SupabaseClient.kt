package com.example.tesy2.data.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.auth.Auth

val supabase = createSupabaseClient(
    supabaseUrl = "https://rnjccfpgdpzkoptzvcgr.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJuamNjZnBnZHB6a29wdHp2Y2dyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI1NzkwMTIsImV4cCI6MjA1ODE1NTAxMn0.rFKKVLNDuDNocuKy_6i4qZijmOdgBl0bAwnecvqslu0"
) {
    install(Postgrest)
    install(Auth)
}
