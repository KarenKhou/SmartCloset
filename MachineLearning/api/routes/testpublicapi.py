from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse, FileResponse
from io import BytesIO
from PIL import Image
import uuid
from rembg import remove
import tempfile
import requests
from fastapi import FastAPI, Request
from fastapi import BackgroundTasks
from ultralytics import YOLO
import os

app = FastAPI()



from MachineLearning.core.config import supabase ,url ,bucket


@app.get("/")
def ping():
    return {"message": "pongg"}

MODEL_PATH = r"C:\Users\User\Desktop\best.pt"
model = YOLO(MODEL_PATH)
print("Loading model from:", MODEL_PATH)
print("Model classes:", model.names)

UPLOAD_DIR = "uploads"
os.makedirs(UPLOAD_DIR, exist_ok=True)



def process_image(image_url: str, item_id: int):
    print("hi")
    try:
        # Download the image
        response = requests.get(image_url)
        if response.status_code == 200:
            print("✅ Image downloaded successfully!")
            
            # Créer un objet BytesIO à partir du contenu de la réponse
            image_data = BytesIO(response.content)
            
            # Vérifie que le contenu est bien téléchargé en tant qu'octets
            print(f"Image data type: {type(image_data)}")

            # Ouvrir l'image avec PIL
            image = Image.open(image_data).convert("RGBA")
            print("Image ouverte avec succès !")

            
            #appliquer le moder et get la categorie
            results = model(image, conf=0.1)
            result = results[0]

            # Extract the top prediction (we assume the first detection is the main label)
            category = model.names[int(result.boxes.cls[0])]
            

            # Appliquer rembg sur les bytes de l'image
            result = remove(image)
            print("removed bg")
            
            with tempfile.NamedTemporaryFile(delete=False, suffix=".png") as tmp:
                result.save(tmp, format="PNG")
                tmp_path = tmp.name
            with open(tmp_path, "rb") as buffer:
                filename = f"rembg_{uuid.uuid4()}.png"
                print(f"Uploading {filename} to Supabase...")

                # Upload vers Supabase Storage
                upload = supabase.storage.from_(bucket).upload(filename, buffer, {"content-type": "image/png"})
                
                print("✅ Image uploaded to Supabase Storage!")
                
            newurl="https://rnjccfpgdpzkoptzvcgr.supabase.co/storage/v1/object/public/picture-clothes//"+filename
            
            print("new url")
            # Update the record with the new image URL
            supabase.table("clothingitem").update({"image_url": newurl,
                                                   "category": category}).eq("item_id", item_id).execute()
            print(f"✅ Image processed and updated for item {item_id}")
        else:
            print(f"❌ Failed to download image. Status code: {response.status_code}")
    except Exception as e:
        print(f"❌ Error processing image for item {item_id}: {str(e)}")

# Endpoint to handle the webhook
@app.post("/webhook/clothingitem")
async def handle_insert_webhook(request: Request, background_tasks: BackgroundTasks):
    # Parse the incoming JSON payload from Supabase
    payload = await request.json()
    record = payload.get("record", {})
    image_url = record.get("image_url")
    item_id = record.get("item_id")

    print(image_url)
    print(item_id)

    # Start the background task for image processing
    background_tasks.add_task(process_image, image_url, item_id)
    print("hi")

    # Return immediately to Supabase
    return {"status": "success", "message": "Image processing started in the background."}


#cd C:\Users\User\Downloads\
#ngrok http 8000


#cd C:\Users\User\Desktop\mdp\smartcloset-api\SmartCloset\
#uvicorn MachineLearning.api.routes.testpublicapi:app --reload



#Attention lezem zid dans la colone:
#category = ANY (ARRAY['top'::text, 'bottom'::text, 'hijabi wear'::text, 'other'::text])



from fastapi import FastAPI, Query
from pydantic import BaseModel
from MachineLearning.imageSimilarity import compdeshabits



class CompareRequest(BaseModel):
    image_url: str
    threshold: float = 0.3

@app.post("/compare")
def compare_image(req: CompareRequest):
    try:
        print("received")
        result = compdeshabits.find_similar_clothing(req.image_url, threshold=req.threshold)
        match_id, similarity = result

        return {
            "status": "ok",
    "match_id": match_id,
    "similarity": similarity,
    "match_found": True
        }
    except Exception as e:
        return {
            "status": "error",
            "message": str(e)
        }
