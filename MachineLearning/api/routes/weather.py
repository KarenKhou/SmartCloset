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