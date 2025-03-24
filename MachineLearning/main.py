import os

os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'  # Suppress TensorFlow warnings
import tensorflow as tf

tf.get_logger().setLevel('ERROR')  # Suppress TensorFlow Hub warnings

import tempfile
import shutil
import logging
import base64
from io import BytesIO
from pathlib import Path
from fastapi import FastAPI, UploadFile, File, Response
from fastapi.responses import JSONResponse, FileResponse
from PIL import Image
from ultralytics import YOLO
from rembg import remove
import python_weather
from recom_screenshot import RecOutfit
import tensorflow_hub as hub

# Configure the logger
logging.basicConfig(filename="app.log", level=logging.INFO, format="%(asctime)s [%(levelname)s] - %(message)s")

# Create a FastAPI application
app = FastAPI(debug=True, title='Fashion AI', summary='This API Provides Access to all Endpoints of Fashion AI Server')

# Load the USE model from TensorFlow Hub
embed = hub.load("./models/universal-sentence-encoder/4")
print("Universal Sentence Encoder model loaded successfully!")

# Add a root endpoint
@app.get("/")
def read_root():
    return {"message": "Welcome to FashionAI!"}

# Handle favicon.ico requests
@app.get("/favicon.ico")
def get_favicon():
    return Response(status_code=204)  # No content response

def extract(source_path):
    model = YOLO("model//best.pt")
    results = model.predict(source=source_path, conf=0.4, save=False, line_width=2)
    class_names = ['sunglass', 'hat', 'jacket', 'shirt', 'pants', 'shorts', 'skirt', 'dress', 'bag', 'shoe']

    source = Image.open(source_path)
    items_list = []
    # Iterate through the detected items and save each one as a separate image
    for item in results:
        # Extract the bounding box coordinates
        x_min, y_min, x_max, y_max = item.boxes.xyxy[0]
        # Crop and save the detected item as a separate image
        detected_item = source.crop((float(x_min), float(y_min), float(x_max), float(y_max)))
        if class_names[int(item.boxes.cls.tolist()[0])].lower() not in ['sunglass', 'hat', 'bag']:
            items_list.append({class_names[int(item.boxes.cls.tolist()[0])]: detected_item})
    return items_list

def remove_bg(img):
    print('inside')
    # Remove the image background using the "rembg" library
    removedBGimage = remove(img)
    print('removing')
    return removedBGimage

@app.post("/remove_background/")
async def remove_background(file: UploadFile = File(...)):
    if not file:
        return JSONResponse(content={"error": "No file uploaded"}, status_code=400)
    try:
        # Create a directory to save the uploaded files if it doesn't exist
        upload_dir = Path("temp")
        upload_dir.mkdir(parents=True, exist_ok=True)

        # Save the uploaded file to the local directory
        file_path = upload_dir / file.filename
        with open(file_path, "wb") as image_file:
            shutil.copyfileobj(file.file, image_file)

        print('going in')
        image = remove_bg(Image.open(file_path))
        print('bg removed')

        # Clean up the uploaded file
        os.remove(file_path)

        # Save the PIL Image as PNG in a temporary file
        with BytesIO() as temp_buffer:
            image.save(temp_buffer, format="PNG")
            temp_buffer.seek(0)

            # Create a temporary file and write the image data to it
            with tempfile.NamedTemporaryFile(delete=False, suffix=".png") as temp_file:
                temp_file.write(temp_buffer.read())
                temp_file_path = temp_file.name

        print('returning')
        return FileResponse(temp_file_path, media_type="image/png",
                            headers={"Content-Disposition": "attachment; filename=removed.png"})

    except Exception as e:
        logging.error(f"Error processing file: {str(e)}")
        return JSONResponse(content={"error": str(e)}, status_code=500)
    finally:
        # Clean up the temporary directory
        shutil.rmtree(upload_dir, ignore_errors=True)

@app.post("/extract/")
async def upload_file(file: UploadFile = File(...)):
    try:
        # Create a directory to save the uploaded files if it doesn't exist
        upload_dir = Path("temp")
        upload_dir.mkdir(parents=True, exist_ok=True)

        # Save the uploaded file to the local directory
        file_path = upload_dir / file.filename
        with open(file_path, "wb") as image_file:
            shutil.copyfileobj(file.file, image_file)

        items_list = extract(file_path)
        images_list = []
        for item in items_list:
            image_name = list(item.keys())[0]
            image = list(item.values())[0]
            image = remove_bg(image)

            # Convert the image to base64
            buffered = BytesIO()
            image.save(buffered, format="PNG")
            img_str = base64.b64encode(buffered.getvalue()).decode("utf-8")

            images_list.append({'name': image_name, 'image': img_str})

        # Clean up the uploaded file
        os.remove(file_path)

        return JSONResponse(content={"message": images_list})
    except Exception as e:
        logging.error(f"Error processing file: {str(e)}")
        return JSONResponse(content={"error": str(e)}, status_code=500)
    finally:
        # Clean up the temporary directory
        shutil.rmtree(upload_dir, ignore_errors=True)


import requests


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

@app.post('/get_recommendation')
async def get_recommendations(file: UploadFile = File(...), Gender: str = None, Occassion: str = None, Season: str = None):
    upload_dir = None
    try:
        # Validate input parameters
        if not Gender or not Occassion or not Season:
            return JSONResponse(
                content={"error": "Gender, Occassion, and Season are required parameters"},
                status_code=400
            )

        # Validate uploaded file
        if not file or file.filename == "":
            return JSONResponse(content={"error": "No file uploaded or file is empty"}, status_code=400)

        # Create a directory to save the uploaded files if it doesn't exist
        upload_dir = Path("temp")
        upload_dir.mkdir(parents=True, exist_ok=True)

        # Save the uploaded file to the local directory
        file_path = upload_dir / file.filename
        with open(file_path, "wb") as image_file:
            shutil.copyfileobj(file.file, image_file)

        # Ensure Gender, Occassion, and Season are not None before calling .lower()
        gender = Gender.lower() if Gender else None
        season = Season.lower() if Season else None
        occassion = Occassion.lower() if Occassion else None

        input_image = {
            'image_path': file_path,
            'Image Tags': {
                'Gender': gender,
                'Season': season,
                'Occassion': occassion
            }
        }

        logging.info(f"Processing file: {file.filename}")
        logging.info(f"Input image data: {input_image}")

        recoutfit = RecOutfit(input_image, 'Wardrobe')
        recommended_outfit, image = recoutfit.controller()

        # Save the PIL Image as JPEG in a temporary file
        with BytesIO() as temp_buffer:
            image.save(temp_buffer, format="JPEG")
            temp_buffer.seek(0)

            # Create a temporary file and write the image data to it
            with tempfile.NamedTemporaryFile(delete=False, suffix=".jpg") as temp_file:
                temp_file.write(temp_buffer.read())
                temp_file_path = temp_file.name

        return FileResponse(temp_file_path, media_type="image/jpeg",
                            headers={"Content-Disposition": "attachment; filename=recommended.png"})
    except Exception as e:
        logging.error(f"Error processing file: {str(e)}")
        return JSONResponse(content={"error": str(e)}, status_code=500)
    finally:
        if upload_dir:  # Check before using it
            shutil.rmtree(upload_dir, ignore_errors=True)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="localhost", port=8000)