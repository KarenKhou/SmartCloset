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


#uvicorn mainApiDef:app --reload
#cd C:\Users\User\Desktop\mdp\smartcloset-api\SmartCloset\MachineLearning\api

# Load the YOLOv8 model
MODEL_PATH = r"C:\Users\User\Desktop\mdp\smartcloset-api\SmartCloset\MachineLearning\api\predictionmodel\best.pt"
model = YOLO(MODEL_PATH)
print("Loading model from:", MODEL_PATH)

print("Model classes:", model.names)
# Ensure upload dir exists
UPLOAD_DIR = "uploads"
os.makedirs(UPLOAD_DIR, exist_ok=True)

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    try:
        # Read image file
        

        image_bytes = await file.read()
        image = Image.open(io.BytesIO(image_bytes)).convert("RGB")

        # Save uploaded image temporarily
        file_id = str(uuid.uuid4())
        input_path = os.path.join(UPLOAD_DIR, f"{file_id}_input.jpg")
        image.save(input_path)

        # Run prediction
        results = model(image, conf=0.1)
        result = results[0]

        # Extract predictions
        detections = []
        for box in result.boxes:
            detections.append({
                "class_id": int(box.cls[0]),
                "confidence": float(box.conf[0]),
                "bbox": box.xyxy[0].tolist()
            })
        print("Raw boxes:", result.boxes)

        print("YOLO detections:")
        for box in result.boxes:
            print(f"→ class: {int(box.cls[0])}, conf: {float(box.conf[0])}, bbox: {box.xyxy[0].tolist()}")


        return {"detections": detections}

    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})



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


@app.get("/getweather/{area}")
async def getweather(area):
    api_key = "c2beb93fb59f3ad3d2358098caa0200b"  # Replace with your actual API key
    url = f"http://api.openweathermap.org/data/2.5/weather?q={area}&appid={api_key}&units=imperial"

    try:
        response = requests.get(url).json()
        print(response)  # Debugging: Print the entire API response

        # Check if the API returned an error
        if "main" not in response:
            return JSONResponse(
                content={
                    "error": "Failed to fetch weather data",
                    "message": response.get("message", "Unknown error")
                },
                status_code=500
            )

        # Access temperature and other weather details
        temperature = response["main"]["temp"]
        temperature = (temperature - 32) * 5 / 9  # Convert Fahrenheit to Celsius

        # Determine the season based on temperature
        if temperature < 25:
            season = 'winter'
        else:
            season = 'summer'

        # Return weather details as a JSON response
        return JSONResponse(
            content={
                "temperature": temperature,
                "season": season,
                "description": response["weather"][0]["description"],
                "kind": response["weather"][0]["main"]
            }
        )
    except Exception as e:
        return JSONResponse(
            content={
                "error": "An error occurred",
                "message": str(e)
            },
            status_code=500
        )