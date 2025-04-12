from fastapi import FastAPI, Form
from fastapi.responses import JSONResponse
from .recom_screenshot import RecOutfit
import random
from fastapi import Request

import traceback
from MachineLearning.core.config import supabase
app = FastAPI()




async def get_outfit_recommendations(
    userid: str = Form(...),
    outfit_type: str = Form(...),
    season: str = Form(...),
    occasion: str = Form(...),
    randomize: bool = Form(False)
):
    outfit_type = outfit_type.replace(" ", "").lower()
    try:
        
        input_tags = {
            'season': season.strip().lower(),
            'occasion': occasion.strip().lower()        }

        recommender = RecOutfit(wardrobe_path='Wardrobe')
        matches = recommender.get_recommendation_by_metadata_only(input_tags,userid)

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