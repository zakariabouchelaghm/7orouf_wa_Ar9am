# ✋ **Hand Count AI**

**Hand Count AI** is an interactive, AI-powered desktop application designed to teach children how to count from **1 to 10** using hand gestures.  

The application displays a number, and the child must show the corresponding number of fingers to the camera. It offers an engaging experience with Arabic language support, fun sounds, and 7 rounds of exercises.

---

## 🌟 **Features**

- **Real-Time Hand Detection** — Utilizes MediaPipe for accurate hand tracking.  
- **Pure NumPy Inference** — Uses a custom Keras (`.h5`) model converted for pure NumPy inference without needing bulky frameworks like TensorFlow.  
- **Child-Friendly Interface** — Modern, dark-themed UI built with CustomTkinter.  
- **Interactive Feedback** — Visual indicators and sound effects celebrate successful answers.  
- **Arabic Support** — Fully translated into Arabic, including numbers, instructions, and feedback messages.  

---

## 🛠️ **Prerequisites & Dependencies**

All required dependencies for this application are listed in the `requirements.txt` file. They include:

- `customtkinter` — UI Framework  
- `opencv-python` — Camera feed  
- `numpy` — Model inference  
- `mediapipe` — Hand landmark detection  
- `Pillow` — Image processing for UI  
- `pygame` — Sound effects  
- `h5py` — Parsing `.h5` model weights  

---

## 🚀 **Getting Started**

### **1. Clone the repository**

Clone the project if you haven't already.

### **2. Install the dependencies**

Run the following command in your terminal:

```bash
pip install -r requirements.txt
