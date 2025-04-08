from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
from io import BytesIO
import cv2
import numpy as np

# Importer ton modèle ML ici
# Par exemple : model = load_model('ton_modele.h5')

app = FastAPI()

@app.post("/detectinout")
async def detect_clothes(file: UploadFile = File(...)):
    # Lire l'image téléchargée
    image_bytes = await file.read()
    np_arr = np.frombuffer(image_bytes, np.uint8)
    image = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)
    
    # Traitement avec le modèle ML (à adapter selon ton modèle)
    # result = model.predict(image)  # Exemple d'utilisation du modèle
    #result = {"clothes_detected": ["T-shirt", "Pantalon"]}  # Remplace avec les résultats de ton modèle
    result={"test in out"}
    return JSONResponse(content=result)
