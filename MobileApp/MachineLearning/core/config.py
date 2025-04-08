from supabase import create_client, Client

url = "https://rnjccfpgdpzkoptzvcgr.supabase.co"
key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJuamNjZnBnZHB6a29wdHp2Y2dyIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MjU3OTAxMiwiZXhwIjoyMDU4MTU1MDEyfQ.-pmxBnHBuJa9Q9ugBbtj9zojuFBY17BJ7J_atcEqCWc"
bucket = "picture-clothes"
supabase: Client = create_client(url, key)
