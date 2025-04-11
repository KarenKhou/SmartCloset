import numpy as np
import cv2
from sklearn.cluster import KMeans

# Ultra-Precise Color Definitions (HSV ranges)
COLOR_PROFILES = {
    "Red": {
        "lower": [[0, 150, 50], [160, 150, 50]], 
        "upper": [[10, 255, 255], [180, 255, 255]],
        "bgr": (0, 0, 255)
    },
    "Green": {
        "lower": [40, 50, 50],
        "upper": [80, 255, 255],
        "bgr": (0, 255, 0)
    },
    "Blue": {
        "lower": [90, 50, 50],
        "upper": [130, 255, 255],
        "bgr": (255, 0, 0)
    },
    "Yellow": {
        "lower": [20, 100, 100],
        "upper": [30, 255, 255],
        "bgr": (0, 255, 255)
    },
    "Orange": {
        "lower": [10, 100, 100],
        "upper": [20, 255, 255],
        "bgr": (0, 165, 255)
    },
    "Purple": {
        "lower": [130, 50, 50],
        "upper": [160, 255, 255],
        "bgr": (128, 0, 128)
    },
    "Pink": {
        "lower": [150, 50, 150],
        "upper": [170, 150, 255],
        "bgr": (203, 192, 255)
    },
    "Brown": {
        "lower": [5, 100, 20],
        "upper": [20, 255, 150],
        "bgr": (42, 42, 165)
    },
    "White": {
        "lower": [0, 0, 200],
        "upper": [180, 30, 255],
        "bgr": (255, 255, 255)
    },
    "Black": {
        "lower": [0, 0, 0],
        "upper": [180, 255, 30],
        "bgr": (0, 0, 0)
    },
    "Gray": {
        "lower": [0, 0, 50],
        "upper": [180, 30, 200],
        "bgr": (128, 128, 128)
    },
    "Teal": {
        "lower": [85, 100, 50],
        "upper": [95, 255, 200],
        "bgr": (128, 128, 0)
    }
}

class PrecisionColorDetector:
    def __init__(self):
        self.min_contour_area = 500
        self.color_verification = True
        self.adaptive_threshold = True
        self.cluster_verification = True

    def detect_dominant_color(self, image_path):
        img = cv2.imread(image_path)
        if img is None:
            print(f"Error loading image: {image_path}")
            return None, None

        # Convert to HSV
        hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
        
        # KMeans clustering for dominant color
        pixels = img.reshape((-1, 3))
        kmeans = KMeans(n_clusters=3, n_init=10).fit(pixels)  # Increased number of clusters for better results
        cluster_centers = kmeans.cluster_centers_
        dominant_color = cluster_centers[np.argmax(np.bincount(kmeans.labels_))]  # Get the most frequent cluster center
        
        # Convert dominant color to HSV
        dominant_color_bgr = dominant_color.astype(int)
        dominant_color_hsv = cv2.cvtColor(np.uint8([[dominant_color_bgr]]), cv2.COLOR_BGR2HSV)[0][0]
        
        # Match dominant color with predefined profiles
        dominant_color_name = self._get_closest_color_name(dominant_color_hsv)
        
        return img, dominant_color_name

    def _get_closest_color_name(self, dominant_hsv):
        closest_color = None
        min_diff = float('inf')
        
        for color_name, profile in COLOR_PROFILES.items():
            # Check if the dominant color is within the predefined color range
            if isinstance(profile["lower"][0], list):  # Multiple ranges
                for l, u in zip(profile["lower"], profile["upper"]):
                    if (l[0] <= dominant_hsv[0] <= u[0] and 
                        l[1] <= dominant_hsv[1] <= u[1] and 
                        l[2] <= dominant_hsv[2] <= u[2]):
                        return color_name
            else:  # Single range
                if (profile["lower"][0] <= dominant_hsv[0] <= profile["upper"][0] and 
                    profile["lower"][1] <= dominant_hsv[1] <= profile["upper"][1] and 
                    profile["lower"][2] <= dominant_hsv[2] <= profile["upper"][2]):
                    return color_name
                
            # Calculate the Euclidean distance between the HSV values of the dominant color and predefined color
            color_bgr = profile["bgr"]
            color_hsv = cv2.cvtColor(np.uint8([[color_bgr]]), cv2.COLOR_BGR2HSV)[0][0]
            diff = np.linalg.norm(dominant_hsv - color_hsv)
            if diff < min_diff:
                min_diff = diff
                closest_color = color_name
        
        return closest_color

def main():
    detector = PrecisionColorDetector()
    
    # Specify your image path
    image_path = r"C:\Users\Lenovo\Desktop\MDP\IMAGES\Screenshot 2025-03-29 120337.png" # Change this
    
    # Process image and detect dominant color
    _, dominant_color = detector.detect_dominant_color(image_path)
    
    if dominant_color:
        print(f"Dominant color detected: {dominant_color}")
    else:
        print("No dominant color detected")

if __name__ == "__main__":
    main()
