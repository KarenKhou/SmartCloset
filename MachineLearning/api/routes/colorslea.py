# color_utils.py
import cv2
import numpy as np
import pandas as pd
from sklearn.cluster import KMeans

# Charger les couleurs
color_df = pd.read_csv("C:/Users/User/Desktop/mdp/smartcloset-api/SmartCloset/MachineLearning/api/routes/colours_rgb_shades(myVersion).csv")

def rgb_to_hex(rgb):
    return '#{:02x}{:02x}{:02x}'.format(rgb[0], rgb[1], rgb[2])

def extract_colors(image_path, num_colors=5):
    image = cv2.imread(image_path)
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

    mask = np.zeros(image.shape[:2], np.uint8)
    bgdModel = np.zeros((1, 65), np.float64)
    fgdModel = np.zeros((1, 65), np.float64)
    rect = (10, 10, image.shape[1]-10, image.shape[0]-10)
    cv2.grabCut(image, mask, rect, bgdModel, fgdModel, 5, cv2.GC_INIT_WITH_RECT)
    mask2 = np.where((mask == 2) | (mask == 0), 0, 1).astype('uint8')
    image_fg = image * mask2[:, :, np.newaxis]

    pixels = image_fg[mask2 == 1]
    kmeans = KMeans(n_clusters=num_colors)
    kmeans.fit(pixels)
    colors = kmeans.cluster_centers_.astype(int)
    counts = np.bincount(kmeans.labels_)
    return colors, counts

def find_closest_color(rgb_tuple):
    hex_color = rgb_to_hex(rgb_tuple).lstrip("#")
    distances = color_df['RGB Hex'].apply(
        lambda x: sum((int(x[i:i+2], 16) - int(hex_color[i:i+2], 16))**2 for i in (0, 2, 4))
        if isinstance(x, str) and len(x) == 6 else float('inf')
    )
    closest_index = distances.idxmin()
    closest_color = color_df.iloc[closest_index]
    return {
        "name": closest_color['Color Name'],
        "hex": closest_color['RGB Hex']
    }
