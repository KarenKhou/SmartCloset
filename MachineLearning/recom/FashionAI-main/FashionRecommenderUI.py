from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QMessageBox, QInputDialog
import requests
import os
import json
import time


class FashionRecommendationUI(QtWidgets.QMainWindow):
    def __init__(self):
        super().__init__()
        self.last_request_time = 0
        self.setWindowTitle("Fashion AI Outfit Recommender")
        self.setFixedSize(1000, 800)  # Slightly larger window

        # Initialize UI components
        self.init_ui()

        # Connect signals
        self.connect_signals()

        # Set style
        self.set_style()

    def init_ui(self):
        """Initialize all UI components"""
        self.central_widget = QtWidgets.QWidget()
        self.setCentralWidget(self.central_widget)

        # Layout
        self.main_layout = QtWidgets.QVBoxLayout()
        self.central_widget.setLayout(self.main_layout)

        # Input Section
        self.create_input_section()

        # Results Section
        self.create_results_section()

        # Status Bar
        self.status_bar = QtWidgets.QStatusBar()
        self.setStatusBar(self.status_bar)

    def create_input_section(self):
        """Create the input controls section"""
        input_group = QtWidgets.QGroupBox("Recommendation Criteria")
        input_layout = QtWidgets.QFormLayout()
        input_layout.setVerticalSpacing(10)
        input_layout.setHorizontalSpacing(15)

        # Gender selection
        self.gender_combo = QtWidgets.QComboBox()
        self.gender_combo.addItems(["Male", "Female"])

        # Season selection
        self.season_combo = QtWidgets.QComboBox()
        self.season_combo.addItems(["Summer", "Winter"])

        # Occasion selection
        self.occasion_combo = QtWidgets.QComboBox()
        self.occasion_combo.addItems(["Casual", "Formal"])

        # Outfit type selection
        self.outfit_type_combo = QtWidgets.QComboBox()
        self.outfit_type_combo.addItems(["Top + Bottom", "Dress"])

        # Get Recommendations button
        self.recommend_button = QtWidgets.QPushButton("Get Recommendations")
        self.recommend_button.setFixedHeight(40)
        self.recommend_button.setStyleSheet("""
            QPushButton {
                background-color: #4CAF50;
                font-weight: bold;
                font-size: 14px;
            }
            QPushButton:hover {
                background-color: #45a049;
            }
        """)

        # Add to layout with improved spacing
        input_layout.addRow(QtWidgets.QLabel("<b>Gender:</b>"), self.gender_combo)
        input_layout.addRow(QtWidgets.QLabel("<b>Season:</b>"), self.season_combo)
        input_layout.addRow(QtWidgets.QLabel("<b>Occasion:</b>"), self.occasion_combo)
        input_layout.addRow(QtWidgets.QLabel("<b>Outfit Type:</b>"), self.outfit_type_combo)
        input_layout.addRow(self.recommend_button)

        input_group.setLayout(input_layout)
        self.main_layout.addWidget(input_group)


    def create_results_section(self):
        """Create the results display section"""
        results_group = QtWidgets.QGroupBox("Recommended Outfits")
        results_layout = QtWidgets.QHBoxLayout()
        results_layout.setSpacing(20)

        # Create outfit display widgets
        self.top_label = QtWidgets.QLabel()
        self.top_label.setAlignment(QtCore.Qt.AlignCenter)
        self.top_label.setFixedSize(300, 400)
        self.top_label.setFrameShape(QtWidgets.QFrame.Box)
        self.set_placeholder_image(self.top_label, "Top")

        self.bottom_label = QtWidgets.QLabel()
        self.bottom_label.setAlignment(QtCore.Qt.AlignCenter)
        self.bottom_label.setFixedSize(300, 400)
        self.bottom_label.setFrameShape(QtWidgets.QFrame.Box)
        self.set_placeholder_image(self.bottom_label, "Bottom")

        self.dress_label = QtWidgets.QLabel()
        self.dress_label.setAlignment(QtCore.Qt.AlignCenter)
        self.dress_label.setFixedSize(300, 400)
        self.dress_label.setFrameShape(QtWidgets.QFrame.Box)
        self.set_placeholder_image(self.dress_label, "Dress")
        self.dress_label.hide()

        # Create stacked widget for bottom/dress
        self.bottom_stack = QtWidgets.QStackedWidget()
        self.bottom_stack.addWidget(self.bottom_label)
        self.bottom_stack.addWidget(self.dress_label)

        # Add widgets to layout
        results_layout.addWidget(self.top_label)
        results_layout.addWidget(self.bottom_stack)

        # Add outfit details section
        details_group = QtWidgets.QGroupBox("Outfit Details")
        details_layout = QtWidgets.QVBoxLayout()
        self.details_text = QtWidgets.QTextEdit()
        self.details_text.setReadOnly(True)
        self.details_text.setFixedWidth(300)
        self.details_text.setFixedHeight(400)
        details_layout.addWidget(self.details_text)
        details_group.setLayout(details_layout)

        # Combine layouts
        combined_layout = QtWidgets.QHBoxLayout()
        combined_layout.addLayout(results_layout)
        combined_layout.addWidget(details_group)

        # Action buttons
        button_layout = QtWidgets.QHBoxLayout()
        button_layout.setSpacing(10)

        self.random_button = QtWidgets.QPushButton("Randomize")
        self.clear_button = QtWidgets.QPushButton("Clear")


        self.random_button.setStyleSheet("background-color: #2196F3; color: white;")
        self.clear_button.setStyleSheet("background-color: #f44336; color: white;")

        button_layout.addWidget(self.random_button)
        button_layout.addWidget(self.clear_button)

        # Set layouts
        results_group.setLayout(combined_layout)
        self.main_layout.addWidget(results_group)
        self.main_layout.addLayout(button_layout)

    def set_placeholder_image(self, label, text):
        """Set placeholder image with text"""
        pixmap = QtGui.QPixmap(label.width(), label.height())
        pixmap.fill(QtGui.QColor(250, 250, 250))  # Lighter background

        painter = QtGui.QPainter(pixmap)
        painter.setPen(QtGui.QColor(150, 150, 150))  # Darker gray text
        font = QtGui.QFont("Arial", 14)
        font.setBold(True)
        painter.setFont(font)
        painter.drawText(pixmap.rect(), QtCore.Qt.AlignCenter, f"{text} will appear here")
        painter.end()

        label.setPixmap(pixmap)
        label.setStyleSheet("""
            QLabel {
                border: 2px solid #ddd;
                border-radius: 5px;
            }
        """)

    def create_outfit_display(self):
        """Create the widgets for displaying outfit items"""
        # Top item
        self.top_label = QtWidgets.QLabel()
        self.top_label.setAlignment(QtCore.Qt.AlignCenter)
        self.top_label.setFixedSize(300, 350)
        self.top_label.setFrameShape(QtWidgets.QFrame.Box)
        self.set_placeholder_image(self.top_label, "Top")

        # Bottom item
        self.bottom_label = QtWidgets.QLabel()
        self.bottom_label.setAlignment(QtCore.Qt.AlignCenter)
        self.bottom_label.setFixedSize(300, 350)
        self.bottom_label.setFrameShape(QtWidgets.QFrame.Box)
        self.set_placeholder_image(self.bottom_label, "Bottom")

        # Dress item
        self.dress_label = QtWidgets.QLabel()
        self.dress_label.setAlignment(QtCore.Qt.AlignCenter)
        self.dress_label.setFixedSize(300, 350)
        self.dress_label.setFrameShape(QtWidgets.QFrame.Box)
        self.set_placeholder_image(self.dress_label, "Dress")
        self.dress_label.hide()

        # Create stacked widget for bottom/dress
        self.bottom_stack = QtWidgets.QStackedWidget()
        self.bottom_stack.addWidget(self.bottom_label)
        self.bottom_stack.addWidget(self.dress_label)

    def set_style(self):
        """Set the application style"""
        self.setStyleSheet("""
            QMainWindow {
                background-color: #f5f5f5;
                font-family: Arial;
            }
            QGroupBox {
                font-size: 14px;
                font-weight: bold;
                border: 1px solid #ddd;
                border-radius: 8px;
                margin-top: 10px;
                padding-top: 15px;
                background-color: white;
            }
            QLabel {
                font-size: 12px;
            }
            QPushButton {
                color: white;
                border: none;
                padding: 8px 16px;
                font-size: 14px;
                border-radius: 4px;
                min-width: 100px;
            }
            QPushButton:hover {
                opacity: 0.9;
            }
            QComboBox {
                padding: 6px;
                font-size: 14px;
                border: 1px solid #ddd;
                border-radius: 4px;
            }
            QTextEdit {
                border: 1px solid #ddd;
                border-radius: 4px;
                padding: 8px;
                font-size: 13px;
                background-color: white;
            }
        """)

    def get_recommendations(self, randomize=False):
        """Get recommendations from the API"""
        now = time.time()
        if now - self.last_request_time < 1.0:  # 1 second cooldown
            return
        self.last_request_time = now
        gender = self.gender_combo.currentText().lower()
        season = self.season_combo.currentText().lower()
        occasion = self.occasion_combo.currentText().lower()
        outfit_type = self.outfit_type_combo.currentText().lower().replace(" ", "")
        print(f"Request Params - G:{gender}, S:{season}, O:{occasion}, OT:{outfit_type}, R:{randomize}")
        try:
            response = requests.post(
                "http://localhost:8000/get_recommendations",
                data={
                    "gender": gender,
                    "season": season,
                    "occasion": occasion,
                    "outfit_type": outfit_type,
                    "randomize": "true" if randomize else "false"
                },
                timeout=5
            )
            if response.status_code == 200:
                data = response.json()
                if data.get("results"):
                    self.display_recommendations(data)
                    # Update status message
                    tops = len(data['results'].get('tops', []))
                    bottoms = len(data['results'].get('bottoms', []))
                    dresses = len(data['results'].get('dresses', []))
                    total = tops + bottoms + dresses
                    msg = f"Found {total} items ({tops} tops, {bottoms} bottoms, {dresses} dresses)"
                    self.status_bar.showMessage(msg, 5000)
                else:
                    self.clear_results()
                    QMessageBox.information(self, "No Results",
                                            "No matching outfits found. Try different criteria.")
            else:
                QMessageBox.warning(self, "Error",
                                    f"Server returned {response.status_code}: {response.text}")

        except requests.exceptions.RequestException as e:
            QMessageBox.critical(self, "Connection Error",
                                 f"Could not connect to server: {str(e)}")

    def display_recommendations(self, data):
        """Display the recommendations in the UI"""
        current_type = self.outfit_type_combo.currentText().lower()
        print(f"UI Current Outfit Type: {current_type} | Backend Data: {data.get('results', {}).keys()}")

        results = data.get("results", {})
        print(f"Raw results: {results}")  # Debug
        if "top + bottom" in self.outfit_type_combo.currentText().lower():
            self.bottom_stack.setCurrentIndex(0)  # Show bottom panel
            self.dress_label.hide()
        self.details_text.clear()

        # Reset display
        self.top_label.hide()
        self.bottom_label.hide()
        self.dress_label.hide()

        outfit_type = self.outfit_type_combo.currentText().lower()

        if "bottom" in outfit_type:  # "top + bottom"
            if results.get("tops") and results.get("bottoms"):
                top = results["tops"][0]
                bottom = results["bottoms"][0]
                self.display_item(self.top_label, top)
                self.display_item(self.bottom_label, bottom)
                self.top_label.show()
                self.bottom_stack.setCurrentIndex(0)
                self.bottom_label.show()
                self.update_details(top, "Top")
                self.update_details(bottom, "Bottom", append=True)
            else:
                self.set_placeholder_image(self.top_label, "No matching tops")
                self.set_placeholder_image(self.bottom_label, "No matching bottoms")
                self.top_label.show()
                self.bottom_stack.setCurrentIndex(0)
                self.bottom_label.show()
        else:  # "dress"
            if results.get("dresses"):
                dress = results["dresses"][0]
                self.display_item(self.dress_label, dress)
                self.dress_label.show()
                self.bottom_stack.setCurrentIndex(1)
                self.update_details(dress, "Dress")
            else:
                self.set_placeholder_image(self.dress_label, "No dresses found")
                self.dress_label.show()
                self.bottom_stack.setCurrentIndex(1)

    def display_item(self, label, item_data):
        """Display an individual clothing item"""
        try:
            image_path = item_data.get("image_path", "")
            if not image_path:
                return self.set_placeholder_image(label, "No image")

            abs_path = os.path.abspath(image_path)
            if not os.path.exists(abs_path):
                return self.set_placeholder_image(label, "Image not found")

            pixmap = QtGui.QPixmap(abs_path)
            if pixmap.isNull():
                return self.set_placeholder_image(label, "Invalid image")

            label.setPixmap(pixmap.scaled(
                label.width(), label.height(),
                QtCore.Qt.KeepAspectRatio,
                QtCore.Qt.SmoothTransformation
            ))

        except Exception as e:
            print(f"Error displaying image: {str(e)}")
            self.set_placeholder_image(label, "Error loading")

    def update_details(self, item, item_type, append=False):
        """Update the details text with item information"""
        metadata = item.get("metadata", {})
        text = f"{item_type}:\n"
        text += f"Color: {metadata.get('basecolour', 'N/A').title()}\n"
        text += f"Gender: {metadata.get('gender', 'N/A').title()}\n"
        text += f"Season: {metadata.get('season', 'N/A').title()}\n"
        text += f"Occasion: {metadata.get('occasion', 'N/A').title()}\n\n"

        if append:
            self.details_text.insertPlainText(text)
        else:
            self.details_text.setPlainText(text)

    def update_display_mode(self):
        """Switch between outfit types based on selection"""
        outfit_type = self.outfit_type_combo.currentText()
        if outfit_type == "Dress":
            self.bottom_stack.setCurrentIndex(1)  # Show dress
        else:
            self.bottom_stack.setCurrentIndex(0)  # Show bottom

    def randomize_outfit(self):
        """Randomize the current outfit selection"""
        # Get current selections
        gender = self.gender_combo.currentText().lower()
        season = self.season_combo.currentText().lower()
        occasion = self.occasion_combo.currentText().lower()
        outfit_type = self.outfit_type_combo.currentText().lower()

        try:
            response = requests.post(
                "http://localhost:8000/get_recommendations",
                data={
                    "gender": gender,
                    "season": season,
                    "occasion": occasion,
                    "outfit_type": outfit_type,
                    "randomize": "true"  # Tell backend to randomize
                },
                timeout=5
            )

            if response.status_code == 200:
                data = response.json()
                if data.get("results"):
                    self.display_recommendations(data)
                    tops = len(data['results'].get('tops', []))
                    bottoms = len(data['results'].get('bottoms', []))
                    dresses = len(data['results'].get('dresses', []))
                    total = tops + bottoms + dresses
                    msg = f"Showing random outfit ({total} options available)"
                    self.status_bar.showMessage(msg, 5000)
                else:
                    QMessageBox.information(self, "No Results",
                                            "No alternative outfits found. Try different criteria.")
            else:
                QMessageBox.warning(self, "Error",
                                    f"Server returned {response.status_code}: {response.text}")

        except requests.exceptions.RequestException as e:
            QMessageBox.critical(self, "Connection Error",
                                 f"Could not connect to server: {str(e)}")

    def clear_results(self):
        """Clear all results"""
        self.set_placeholder_image(self.top_label, "Top")
        self.set_placeholder_image(self.bottom_label, "Bottom")
        self.set_placeholder_image(self.dress_label, "Dress")
        self.bottom_stack.setCurrentIndex(0)  # Show bottom by default
        self.details_text.clear()
        self.status_bar.clearMessage()


    def connect_signals(self):
        """Connect UI signals to slots"""
        self.recommend_button.clicked.connect(lambda: self.get_recommendations(randomize=False))
        self.random_button.clicked.connect(lambda: self.get_recommendations(randomize=True))
        self.clear_button.clicked.connect(self.clear_results)
        self.outfit_type_combo.currentTextChanged.connect(self.update_display_mode)

        # Connect other signals as needed
        self.gender_combo.currentTextChanged.connect(self.clear_results)
        self.season_combo.currentTextChanged.connect(self.clear_results)
        self.occasion_combo.currentTextChanged.connect(self.clear_results)

def run_ui():
    """Run the application"""
    import sys
    app = QtWidgets.QApplication(sys.argv)
    app.setStyle("Fusion")

    # Create and show main window
    window = FashionRecommendationUI()
    window.show()

    sys.exit(app.exec_())


if __name__ == "__main__":
    run_ui()