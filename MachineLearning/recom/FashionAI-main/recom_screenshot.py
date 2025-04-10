import logging
import os
import pandas as pd
import json
from color_utils import get_color_combinations  # Import the color utility


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
        self.bottom_categories = ['pant', 'skirt', 'short']
        self.dress_categories = ['dress']


    def load_wardrobe_tags(self):
        """Load and clean wardrobe tags"""
        with open(self.wardrobe_tagfile_path) as f:
            data = json.load(f)

        df = pd.DataFrame(data)

        # Normalize column names to lowercase
        df.columns = df.columns.str.lower()

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
        print(f"Filtering for: {input_tags}")
        tags_df = self.load_wardrobe_tags()

        # First filter for tops that match the criteria
        top_mask = pd.Series(True, index=tags_df.index)
        for col, value in input_tags.items():
            if col in tags_df.columns:
                top_mask &= (tags_df[col] == value.strip().lower())

        # Get matching tops
        top_matches = tags_df[top_mask & tags_df['image_id'].apply(
            lambda x: self.classify_item(x) == 'top'
        )]

        if top_matches.empty:
            return []

        # Select a random top (or first one)
        top_row = top_matches.sample(1).iloc[0]
        top_color = top_row['basecolour']

        # Get compatible bottom colors
        compatible_bottom_colors = get_color_combinations(top_color)
        print(f"Top color: {top_color}, Compatible bottom colors: {compatible_bottom_colors}")

        # Filter bottoms that match criteria AND color
        bottom_mask = pd.Series(True, index=tags_df.index)
        for col, value in input_tags.items():
            if col in tags_df.columns:
                bottom_mask &= (tags_df[col] == value.strip().lower())

        bottom_mask &= tags_df['image_id'].apply(
            lambda x: self.classify_item(x) == 'bottom'
        )

        # Add color compatibility filter
        bottom_mask &= tags_df['basecolour'].isin(compatible_bottom_colors)

        # Get matching bottoms
        bottom_matches = tags_df[bottom_mask]

        results = []
        # Add top to results
        top_img_path = os.path.abspath(os.path.join(self.wardrobe_path, top_row['image_id']))
        if os.path.exists(top_img_path):
            top_img_path = top_img_path.replace("\\", "/")
            top_metadata = top_row.to_dict()
            top_metadata['category'] = 'top'
            results.append({
                'metadata': top_metadata,
                'image_path': top_img_path
            })

        # Add bottoms to results
        for _, bottom_row in bottom_matches.iterrows():
            bottom_img_path = os.path.abspath(os.path.join(self.wardrobe_path, bottom_row['image_id']))
            if os.path.exists(bottom_img_path):
                bottom_img_path = bottom_img_path.replace("\\", "/")
                bottom_metadata = bottom_row.to_dict()
                bottom_metadata['category'] = 'bottom'
                results.append({
                    'metadata': bottom_metadata,
                    'image_path': bottom_img_path
                })

        return results