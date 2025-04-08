import numpy as np
import cv2
import webcolors


def closest_color(requested_color):
    min_colors = {}
    for key, name in webcolors.CSS3_HEX_TO_NAMES.items():
        r_c, g_c, b_c = webcolors.hex_to_rgb(key)
        distance = (r_c - requested_color[0])**2 + (g_c - requested_color[1])**2 + (b_c - requested_color[2])**2
        min_colors[distance] = name
    return min_colors[min(min_colors.keys())]

def remove_background(image):
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    mask = cv2.adaptiveThreshold(gray, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 11, 2)
    kernel = np.ones((3, 3), np.uint8)
    mask = cv2.morphologyEx(mask, cv2.MORPH_OPEN, kernel, iterations=2)
    result = cv2.bitwise_and(image, image, mask=mask)
    return result

def detect_dominant_color(image_bytes: bytes) -> str:
    file_bytes = np.frombuffer(image_bytes, np.uint8)
    img = cv2.imdecode(file_bytes, cv2.IMREAD_UNCHANGED)

    if img is None:
        return "Error loading image"

    if img.shape[-1] == 4:
        alpha_channel = img[:, :, 3]
        mask = alpha_channel > 128
        img = img[:, :, :3]
    else:
        mask = np.ones(img.shape[:2], dtype=bool)

    img_no_bg = remove_background(img)
    pixels = img_no_bg.reshape((-1, 3))
    pixels = np.array([p for p in pixels if np.any(p > 30)], dtype=np.float32)

    if len(pixels) == 0:
        pixels = img.reshape((-1, 3)).astype(np.float32)

    number_clusters = 5
    criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 10, 1.0)
    flags = cv2.KMEANS_RANDOM_CENTERS
    _, labels, centers = cv2.kmeans(pixels, number_clusters, None, criteria, 10, flags)

    unique, counts = np.unique(labels, return_counts=True)
    dominant_index = unique[np.argmax(counts)]
    dominant_color = centers[dominant_index]

    color_name = closest_color((int(dominant_color[2]), int(dominant_color[1]), int(dominant_color[0])))

    if color_name == "black":
        if dominant_color[0] > dominant_color[1] and dominant_color[0] > dominant_color[2]:
            color_name = "blue"

    return color_name
