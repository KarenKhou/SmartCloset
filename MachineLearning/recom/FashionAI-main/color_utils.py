# color_utils.py
import random


def get_color_combinations(top_color):
    """
    Get recommended bottom colors based on top color
    Returns a list of compatible bottom colors
    """
    # Define color groups and their relationships
    color_groups = {
        'red': ['black', 'white', 'blue', 'beige'],
        'blue': ['white', 'beige', 'black', 'red', 'purple', 'brown'],
        'black': ['white', 'beige', 'red', 'blue', 'pink', 'purple', 'brown','black'],
        'white': ['black', 'blue', 'red', 'pink', 'beige', 'purple', 'brown'],
        'beige': ['black', 'blue', 'red', 'white', 'purple', 'brown'],
        'pink': ['black', 'white', 'beige', 'blue', 'purple', 'brown'],
        'orange': ['black', 'white', 'blue', 'brown'],
        'yellow': ['black', 'white', 'blue', 'brown'],
        'green': ['black', 'white', 'beige', 'brown'],
        'purple': ['white', 'black', 'beige', 'gray', 'pink', 'light blue', 'brown'],
        'brown': ['beige', 'white', 'black', 'blue', 'green', 'orange', 'yellow'],
        'multi': ['black', 'white', 'beige', 'brown']
    }

    # Find the base color (simplified matching)
    top_color = top_color.lower()
    base_color = None

    # Check for direct matches
    for color in color_groups:
        if color in top_color:
            base_color = color
            break

    # If no direct match, try to find similar colors
    if base_color is None:
        if 'grey' in top_color or 'gray' in top_color:
            base_color = 'black'
        elif 'navy' in top_color:
            base_color = 'blue'
        elif 'cream' in top_color or 'tan' in top_color:
            base_color = 'beige'
        elif 'lavender' in top_color or 'lilac' in top_color or 'violet' in top_color:
            base_color = 'purple'
        elif 'chocolate' in top_color or 'camel' in top_color or 'taupe' in top_color:
            base_color = 'brown'
        else:
            # Default to black or white for unknown colors
            base_color = random.choice(['black', 'white'])

    # Return compatible bottom colors
    return color_groups.get(base_color, ['black', 'white', 'beige'])