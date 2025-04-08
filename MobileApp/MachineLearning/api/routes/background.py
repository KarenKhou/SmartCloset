from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse, FileResponse
from ultralytics import YOLO
from PIL import Image
import io
import uuid
import os
from rembg import remove
import tempfile
app = FastAPI()
import requests


@app.post("/remove_background")
async def remove_background(file: UploadFile = File(...)):
    try:
        image_bytes = await file.read()
        image = Image.open(io.BytesIO(image_bytes)).convert("RGBA")

        # Remove background
        removed_bg = remove(image)

        # Save to temp file
        with tempfile.NamedTemporaryFile(delete=False, suffix=".png") as tmp:
            removed_bg.save(tmp, format="PNG")
            tmp_path = tmp.name

        return FileResponse(tmp_path, media_type="image/png", filename="removed_background.png")

    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})
