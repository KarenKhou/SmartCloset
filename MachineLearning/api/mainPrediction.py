from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
from ultralytics import YOLO
from PIL import Image
import io
import uuid
import os

app = FastAPI()

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
