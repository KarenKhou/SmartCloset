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

app = FastAPI()



from MachineLearning.core.config import supabase , url,bucket


@app.get("/")
def ping():
    return {"message": "pongg"}

# @app.post("/webhook/clothingitemmmm")
# async def handle_insert_webhook(request: Request):
#     payload = await request.json()
#     record = payload.get("record", {})  # Supabase envoie le nouvel objet ici
#     print(record)

#     image_url = record.get("image_url")
#     item_id = record.get("id")

#     print(image_url)
#     if not image_url or not item_id:
#         return {"error": "Missing image_url or id"}
#     print("hi")
#     response = requests.get(image_url)
#     print("hii")
#     image = Image.open(BytesIO(response.content)).convert("RGBA")
#     image.show() 
#     print("Image opened successfully!")
        
    
    
    
    
#     try:
#         result = remove(image)
#         print("Background removed successfully!")
#     except Exception as e:
#         print(f"Error during background removal: {e}")


#     # Uploader sur Supabase Storage
#     buffer = BytesIO()
#     result.save(buffer, format="PNG")
#     buffer.seek(0)
#     filename = f"rembg_{uuid.uuid4()}.png"
#     print(filename)

#     supabase.storage.from_(bucket).upload(filename, buffer, {"content-type": "image/png"})
#     print("upload reussit")
#     # Générer l'URL publique
#     new_url = f"{url}/storage/v1/object/public/{bucket}/{filename}"
#     print(new_url)
#     # Mettre à jour l'enregistrement
#     supabase.table("clothingitem").update({
#          "image_url": new_url
#      }).eq("id", item_id).execute()

#     return {"status": "success", "new_url": new_url}

    



#cd "C:\Users\User\Downloads\
#ngrok http 8000


#cd C:\Users\User\Desktop\mdp\smartcloset-api\SmartCloset\
#uvicorn MachineLearning.api.routes.testpublicapi:app --reload


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

            
            # Convertir l'image PIL en bytes
            

            # Appliquer rembg sur les bytes de l'image
            result = remove(image)
            print("removed bg")
            # Upload to Supabase Storage
            # buffer = BytesIO()
            # print("test")
            # result.save(buffer, format="PNG")
            # print("test2")
            # buffer.seek(0)
            # print("Test3")
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
            supabase.table("clothingitem").update({"image_url": newurl}).eq("item_id", item_id).execute()
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