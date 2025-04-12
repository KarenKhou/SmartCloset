from fastapi import FastAPI, Form
from fastapi.responses import JSONResponse
from .recom_screenshot import RecOutfit
import random

import random
import traceback
from MachineLearning.core.config import supabase
app = FastAPI()





@app.post('/get_recommendations')
async def get_recommendations(
    outfit_type: str = Form(...),
    gender: str = Form(...),
    season: str = Form(...),
    occasion: str = Form(...),
    randomize: bool = Form(False)  # New parameter for randomization
):
    outfit_type = outfit_type.replace(" ", "").lower()
    print(f"Backend received outfit_type: {outfit_type}")
    try:
        input_tags = {
            'gender': gender.strip().lower(),
            'season': season.strip().lower(),
            'occasion': occasion.strip().lower()
        }

        recommender = RecOutfit(wardrobe_path='Wardrobe')
        matches = recommender.get_recommendation_by_metadata_only(input_tags)

        if not matches:
            return JSONResponse(
                status_code=200,
                content={"message": "No matches found", "results": {}}
            )

        # Group results by category
        categorized = {
            'tops': [m for m in matches if m['metadata']['category'] == 'top'],
            'bottoms': [m for m in matches if m['metadata']['category'] == 'bottom'],
            'dresses': [m for m in matches if m['metadata']['category'] == 'dress']
        }

        # For randomization, shuffle the lists
        if randomize:
            random.shuffle(categorized['tops'])
            random.shuffle(categorized['bottoms'])
            random.shuffle(categorized['dresses'])

        if outfit_type == "top+bottom":
            if not categorized['tops']:
                print("DEBUG: No tops found for criteria")
            if not categorized['bottoms']:
                print("DEBUG: No bottoms found for criteria")

            return JSONResponse(content={
                "results": {
                    'tops': categorized['tops'][:1] if categorized['tops'] else [],
                    'bottoms': categorized['bottoms'][:1] if categorized['bottoms'] else []
                }
            })
        elif outfit_type == "dress":
            return JSONResponse(content={
                "results": {
                    'dresses': categorized['dresses'][:1] if categorized['dresses'] else []
                }
            })

    except Exception as e:
        return JSONResponse(
            status_code=400,
            content={"error": str(e)}
        )
    print(f"Filtered results - Tops: {len(categorized['tops'])}, Bottoms: {len(categorized['bottoms'])}, Dresses: {len(categorized['dresses'])}")
# if __name__ == "__main__":
#     import uvicorn
#     uvicorn.run(app, host="0.0.0.0", port=8000)


    # MachineLearning/recommendation.py

# from fastapi import Form
# from fastapi.responses import JSONResponse
# from recom_screenshot import RecOutfit
# 

async def get_outfit_recommendations(
    outfit_type: str,
    gender: str,
    season: str,
    occasion: str,
    randomize: bool = False
):
    outfit_type = outfit_type.replace(" ", "").lower()
    try:
        input_tags = {
            'gender': gender.strip().lower(),
            'season': season.strip().lower(),
            'occasion': occasion.strip().lower()        }

        recommender = RecOutfit(wardrobe_path='Wardrobe')
        matches = recommender.get_recommendation_by_metadata_only(input_tags)

        if not matches:
            return JSONResponse(
                status_code=200,
                content={"message": "No matches found", "results": {}}
            )

        # categorized = {
        #     'tops': [m for m in matches if m['metadata']['category'] == 'top'],
        #     'bottoms': [m for m in matches if m['metadata']['category'] == 'bottom'],
        #     'dresses': [m for m in matches if m['metadata']['category'] == 'dress']
        # }

        categorized = {
            'tops': [m for m in matches if m['category'] == 'top'],
            'bottoms': [m for m in matches if m['category'] == 'bottom'],
            'dresses': [m for m in matches if m['category'] == 'dress']
        }


        if randomize:
            random.shuffle(categorized['tops'])
            random.shuffle(categorized['bottoms'])
            random.shuffle(categorized['dresses'])

        if outfit_type == "top+bottom":

            resu = categorized['tops'][:1] + categorized['bottoms'][:1]
            print("✅ Top selected:", categorized['tops'][:1])
            print("✅ Bottom selected:", categorized['bottoms'][:1])


            
                
            user = supabase.auth.get_user()
            userid = user.id if user else "b5d6de82-e003-4748-b1d4-b826d658761b"


            reco_resp = supabase.table("outfitrecommendation").insert({
                "user_id": userid,
                
                # "weather_condition_id": weather_condition_id,
                "style": input_tags.get('occasion')
            }).execute()
            print("Reco response:", reco_resp.data)

            recommendation_id = reco_resp.data[0]["recommendation_id"]

            # 🔍 DEBUG : check for duplicates
            unique_items = {item['item_id']: item for item in resu}.values()
            to_insert = [
                {
                "recommendation_id": int(recommendation_id),
                "item_id": int(item["item_id"]),
            }
            for item in unique_items
            ]
            print(f"📦 [DEBUG] Final unique items: {to_insert}")

            
            print("📦 [DEBUG] To insert in outfitrecommendation_item:", to_insert)



            supabase.table("outfitrecommendation_items").insert(to_insert).execute()
            return JSONResponse(content={
                "results": {
                    'tops': categorized['tops'][:1],
                    'bottoms': categorized['bottoms'][:1]
                }
            })
        

        elif outfit_type == "dress":
            return JSONResponse(content={
                "results": {
                    'dresses': categorized['dresses'][:1]
                }
            })
        


    except Exception as e:
        traceback.print_exc()  # affiche le vrai stack trace dans la console
        return JSONResponse(
            status_code=400,
            content={"error": repr(e)}  # montre l’objet d’exception réel
        )