import logging
import os
import pandas as pd
import json
from .color_utils import get_color_combinations  # Import the color utility
import traceback

from MachineLearning.core.config import supabase

logging.basicConfig(
    filename="recom_screenshot.log",
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] - %(message)s"
)

class RecOutfit:
    def __init__(self, wardrobe_path):
        self.wardrobe_path = wardrobe_path
        self.wardrobe_tagfile_path = 'clothing_data.json'
        self.top_categories = ['shirt', 'tshirt', 'sweater', 'jacket']
        self.bottom_categories = ['pants', 'skirt', 'short']
        self.dress_categories = ['dress']


    def load_wardrobe_tags(self):
        # """Load and clean wardrobe tags"""
        # with open(self.wardrobe_tagfile_path) as f:
        #     data = json.load(f)

        # df = pd.DataFrame(data)

        # # Normalize column names to lowercase
        # df.columns = df.columns.str.lower()

        
        user = supabase.auth.get_user()
        userid = user.id if user else "b5d6de82-e003-4748-b1d4-b826d658761b"

        closetsid=supabase.table("closet").select("closet_id").eq("user_id",userid).execute()
        closet_ids = [row["closet_id"] for row in closetsid.data]
        response = supabase.table("clothingitem").select("item_id","category","season","style","color").eq("availability",1).in_("closet_id",closet_ids).execute()
        data = response.data

        df = pd.DataFrame(data)

        if df.empty:
            logging.warning("No data returned from Supabase.")
            return pd.DataFrame()

        # Clean data values
        string_cols = df.select_dtypes(include=['object']).columns
        df[string_cols] = df[string_cols].apply(lambda x: x.str.lower().str.strip())

        return df

    def classify_item(self, image_id):
        """Determine clothing type ONLY based on filename"""
        image_id = image_id.lower()
        if 'shirt' in image_id or 'sweater' in image_id or 'jacket' in image_id or 'tshirt' in image_id:
            return 'top'
        elif 'pant' in image_id or 'short' in image_id or 'skirt' in image_id:
            return 'bottom'
        elif 'dress' in image_id:
            return 'dress'
        return 'other'

    def get_recommendation_by_metadata_only(self, input_tags):
        try:
            print(f"Filtering for: {input_tags}")
            tags_df = self.load_wardrobe_tags()

            # First filter for tops that match the criteria
            # top_mask = pd.Series(True, index=tags_df.index)
            # for col, value in input_tags.items():
            #     if col in tags_df.columns:
            #         top_mask &= (tags_df[col] == value.strip().lower())


            top_mask = pd.Series(True, index=tags_df.index)

            for col, value in input_tags.items():
                if col not in tags_df.columns:
                    continue

                value = value.strip().lower()

                if col == "season":
                    top_mask &= tags_df[col].apply(
                        lambda s: s == value or (s in ["spring", "autumn"] and value in ["summer", "winter"])
                    )

                elif col == "occasion":
                    top_mask &= tags_df[col].apply(
                        lambda o: o == value or o == "both"
                    )

                else:
                    top_mask &= tags_df[col] == value


                    

            print(f"🔍 [DEBUG] Top mask: {top_mask.sum()} matches")

            # Get matching tops
            top_matches = tags_df[top_mask & tags_df['category'].apply(
                lambda x: self.classify_item(x) == 'top'
            )]

            if top_matches.empty:
                print("🚫 [DEBUG] No top matches found.")
                return []

            # Select a random top (or first one)
            top_row = top_matches.sample(1).iloc[0]
            top_color = top_row['color']

            # Get compatible bottom colors
            compatible_bottom_colors = get_color_combinations(top_color)
            print(f"Top color: {top_color}, Compatible bottom colors: {compatible_bottom_colors}")

            # Filter bottoms that match criteria AND color
            # bottom_mask = pd.Series(True, index=tags_df.index)
            # for col, value in input_tags.items():
            #     if col in tags_df.columns:
            #         bottom_mask &= (tags_df[col] == value.strip().lower())

            # bottom_mask &= tags_df['category'].apply(
            #     lambda x: self.classify_item(x) == 'bottom'
            # )

            bottom_mask = pd.Series(True, index=tags_df.index)

            for col, value in input_tags.items():
                if col not in tags_df.columns:
                    continue

                value = value.strip().lower()

                if col == "season":
                    bottom_mask &= tags_df[col].apply(
                        lambda s: s == value or (s in ["spring", "autumn"] and value in ["summer", "winter"])
                    )

                elif col == "occasion":
                    bottom_mask &= tags_df[col].apply(
                        lambda o: o == value or o == "both"
                    )

                else:
                    bottom_mask &= tags_df[col] == value


            # Add color compatibility filter
            bottom_mask &= tags_df['color'].isin(compatible_bottom_colors)

            bottom_matches = tags_df[bottom_mask]
            print(f"🩳 [DEBUG] Found {len(bottom_matches)} bottom matches")

            results = [{"item_id": top_row["item_id"], "category": "top"}]
            for _, row in bottom_matches.iterrows():
                results.append({"item_id": row["item_id"], "category": "bottom"})

            print(f"✅ [DEBUG] Final results: {results}")
        except Exception as e:
            traceback.print_exc()
            print(f"❌ [ERROR] Exception during recommendation: {repr(e)}")
            return []
        # Top item
        results.append({
            "item_id": top_row["item_id"],
            "category": "top"
        })

        # Bottoms
        for _, bottom_row in bottom_matches.iterrows():
            results.append({
                "item_id": bottom_row["item_id"],
                "category": "bottom"
            })


            

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
        unique_items = {item['item_id']: item for item in results}.values()
        to_insert = [
            {
            "recommendation_id": int(recommendation_id),
            "item_id": int(item["item_id"]),
        }
        for item in unique_items
        ]
        logging.debug(f"📦 [DEBUG] Final unique items: {to_insert}")



    #     to_insert = [
    # {
    #     "recommendation_id": int(recommendation_id),  # au cas où
    #     "item_id": int(item["item_id"]),  # conversion nécessaire ici
    # }
    # for item in results]
        
        print("📦 [DEBUG] To insert in outfitrecommendation_item:", to_insert)



        supabase.table("outfitrecommendation_items").insert(to_insert).execute()

        # Convert all item_id to native Python int
        for item in results:
            item["item_id"] = int(item["item_id"])

        return results
