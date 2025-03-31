import cv2
import numpy as np
import requests
from io import BytesIO
from skimage.metrics import structural_similarity as structural_similarity
from skimage.transform import resize
from supabase import create_client, Client

# Supabase setup
url = "https://rnjccfpgdpzkoptzvcgr.supabase.co"
key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJuamNjZnBnZHB6a29wdHp2Y2dyIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MjU3OTAxMiwiZXhwIjoyMDU4MTU1MDEyfQ.-pmxBnHBuJa9Q9ugBbtj9zojuFBY17BJ7J_atcEqCWc"
supabase: Client = create_client(url, key)

# Download image from URL
def download_image(url):
    response = requests.get(url)
    if response.status_code == 200:
        image_data = np.frombuffer(response.content, np.uint8)
        return cv2.imdecode(image_data, cv2.IMREAD_GRAYSCALE)
    else:
        return None

# ORB similarity
def orb_sim(img1, img2):
    orb = cv2.ORB_create()
    kp1, desc1 = orb.detectAndCompute(img1, None)
    kp2, desc2 = orb.detectAndCompute(img2, None)

    if desc1 is None or desc2 is None:
        return 0.0

    bf = cv2.BFMatcher(cv2.NORM_HAMMING, crossCheck=True)
    matches = bf.match(desc1, desc2)
    similar = [m for m in matches if m.distance < 50]

    if len(matches) == 0:
        return 0.0

    return len(similar) / len(matches)

# Main function
def find_similar_clothing(input_image_url, threshold=0.3):
    input_img = download_image(input_image_url)
    if input_img is None:
        raise Exception("Image de départ non chargée.")

    clothing_items = supabase.table("clothingitem").select("item_id, image_url").execute().data
    best_similarity=0.0
    for item in clothing_items:
        image_url = item.get("image_url")
        if not image_url or not image_url.startswith("http"):
            print(f"Image URL invalide pour l'item {item['item_id']}, on passe.")
            continue
        other_img = download_image(item["image_url"])
        if other_img is None:
            continue

        sim = orb_sim(input_img, other_img)
        print(f"Comparaison avec {item['item_id']} → Similarité : {sim:.2f}")

        if sim > best_similarity:
            best_similarity = sim
            best_match_id = item["item_id"]

    

        print(f"Meilleure similarité : {best_similarity:.2f} avec l’ID {best_match_id}")
        #if best_similarity >= threshold:
         #   return item["item_id"]

    return best_match_id,best_similarity  # Aucun match trouvé



#input_url = "https://rnjccfpgdpzkoptzvcgr.supabase.co/storage/v1/object/public/twosecpic//photo_1743413239333.png"

#matched_id = find_similar_clothing(input_url)

#if matched_id:
#    print("Same clothing item found! ID:", matched_id)
#else:
#    print("No similar item found.")

