import numpy as np
import cv2
from sklearn.cluster import KMeans
import os 

# Ultra-Precise Color Definitions (HSV ranges)
COLOR_PROFILES = {
    # Primary Colors
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
    
    # Secondary Colors
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
    
    # Neutral Colors
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
    
    # Special Universal Color
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

    def detect_colors(self, image_path):
        img = cv2.imread(image_path)
        if img is None:
            print(f"Error loading image: {image_path}")
            return None

        # Advanced preprocessing
        hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
        hsv[:,:,2] = cv2.createCLAHE(clipLimit=3.0, tileGridSize=(8,8)).apply(hsv[:,:,2])
        
        output = img.copy()
        detected = []

        for color_name, profile in COLOR_PROFILES.items():
            mask = self._create_color_mask(hsv, profile)
            contours = self._find_significant_contours(mask)
            
            for cnt in contours:
                x,y,w,h = cv2.boundingRect(cnt)
                roi = img[y:y+h, x:x+w]
                
                if self._verify_color(roi, profile):
                    self._draw_detection(output, color_name, profile["bgr"], x, y, w, h)
                    detected.append(color_name)
        
        return output, list(set(detected))

    def _create_color_mask(self, hsv_img, profile):
        mask = np.zeros(hsv_img.shape[:2], dtype=np.uint8)
        lower = profile["lower"]
        upper = profile["upper"]
        
        # Handle single or multiple ranges
        if isinstance(lower[0], list):
            for l, u in zip(lower, upper):
                mask = cv2.bitwise_or(mask, cv2.inRange(hsv_img, np.array(l), np.array(u)))
        else:
            mask = cv2.inRange(hsv_img, np.array(lower), np.array(upper))
            
        # Morphological refinement
        kernel = cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (5,5))
        mask = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel, iterations=2)
        mask = cv2.morphologyEx(mask, cv2.MORPH_OPEN, kernel, iterations=1)
        
        return mask

    def _find_significant_contours(self, mask):
        contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        return [cnt for cnt in contours if cv2.contourArea(cnt) > self.min_contour_area]

    def _verify_color(self, roi, profile):
        if not self.color_verification:
            return True
            
        # Dominant color analysis
        pixels = roi.reshape((-1,3))
        kmeans = KMeans(n_clusters=2, n_init=10).fit(pixels)
        dominant = kmeans.cluster_centers_[np.argmax(np.bincount(kmeans.labels_))]
        
        # Convert to HSV for verification
        dominant_hsv = cv2.cvtColor(np.uint8([[dominant]]), cv2.COLOR_BGR2HSV)[0][0]
        
        # Check against profile ranges
        if isinstance(profile["lower"][0], list):  # Multiple ranges
            for l, u in zip(profile["lower"], profile["upper"]):
                if (l[0] <= dominant_hsv[0] <= u[0] and 
                    l[1] <= dominant_hsv[1] <= u[1] and 
                    l[2] <= dominant_hsv[2] <= u[2]):
                    return True
        else:  # Single range
            if (profile["lower"][0] <= dominant_hsv[0] <= profile["upper"][0] and 
                profile["lower"][1] <= dominant_hsv[1] <= profile["upper"][1] and 
                profile["lower"][2] <= dominant_hsv[2] <= profile["upper"][2]):
                return True
                
        return False

    def _draw_detection(self, image, text, color, x, y, w, h):
        cv2.rectangle(image, (x,y), (x+w,y+h), color, 2)
        
        # Smart label positioning
        (tw, th), _ = cv2.getTextSize(text, cv2.FONT_HERSHEY_SIMPLEX, 0.7, 2)
        label_y = y - 10 if y > th + 20 else y + h + th + 10
        label_y = max(th + 10, min(label_y, image.shape[0] - 10))
        
        # Label background
        cv2.rectangle(image, 
                     (x, label_y-th-10),
                     (x+tw+10, label_y+10),
                     color, -1)
        
        # Text with contrast
        text_color = (255,255,255) if np.mean(color) < 127 else (0,0,0)
        cv2.putText(image, text, 
                   (x+5, label_y), 
                   cv2.FONT_HERSHEY_SIMPLEX, 0.7, text_color, 2)



def main():
    detector = PrecisionColorDetector()
    detector.min_contour_area = 800
    
    # Specify your image path
    image_path = r"C:\Users\Admin\Desktop\fatima\mdp\example7.jpg"  # Change this
    
    # Process image (no GUI, no file saving)
    _, detected_colors = detector.detect_colors(image_path)
    
    if not detected_colors:
        print("No colors detected")
        return
    
    # Only print detected colors
    print("Detected colors:")
    for color in sorted(detected_colors):
        print(color)


if __name__ == "__main__":
    main()
