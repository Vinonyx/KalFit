# KalFit – Calorie Prediction Mobile App

KalFit is a health-focused mobile application built using **Kotlin** that helps users estimate the calorie content of food based on its nutritional values. It integrates a machine learning model trained using **Linear Regression** to provide fast and accurate calorie predictions.

## 📱 Features
- 🧮 **Calorie Prediction**: Estimate calorie count based on fat, carbohydrates, and protein input.
- 📊 **Machine Learning Integration**: Uses a model trained with scikit-learn and exported for mobile use.
- 💡 **Simple UI**: Clean and intuitive user interface designed with Android best practices.
- 🚀 **Fast Predictions**: Local model inference ensures quick response without needing internet.

## 🔍 How It Works
1. The user enters nutritional values (protein, fat, carbohydrates).
2. The app uses a trained **Linear Regression** model to predict the total calorie value.
3. The prediction is shown immediately on the screen.

## 🧠 Machine Learning Details
- **Model Used**: Linear Regression (scikit-learn)
- **Training Data**: Food nutrition dataset with features:
  - `Protein (g)`
  - `Fat (g)`
  - `Carbohydrates (g)`
  - `Calories (target)`
- **Evaluation Metrics**:
  - R² Score: ~0.92
  - MAE: Low
- **Exported Format**: `.pkl` or converted to mobile-compatible format (e.g., TFLite)

## 🛠️ Tech Stack
- **Frontend**: Kotlin (Android Studio)
- **Machine Learning**: Python, scikit-learn, pandas
- **Model Integration**: TFLite

## 🧪 Example
| Input              | Value |
|--------------------|-------|
| Protein (g)        | 10    |
| Fat (g)            | 5     |
| Carbohydrates (g)  | 20    |
| **Predicted Calories** | 170 kcal |

## 📂 Project Structure
KalFit/
├── app/ # Kotlin-based Android mobile application (UI & logic)
├── Backend/ # Python ML model & API logic (calorie prediction)
│ └── Project_ML_LR.ipynb # Jupyter Notebook for training & evaluation
├── .gitignore # Git ignore rules
├── build.gradle.kts # Gradle Kotlin DSL build file
├── settings.gradle.kts # Gradle project settings
├── gradle/ # Gradle wrapper and settings
├── gradlew / gradlew.bat # Gradle wrappers

## 📦 How to Run
### 🔹 ML Model (Python)
1. Navigate to `/Backend/`
2. Open and run `Project_ML_LR.ipynb` to train and test the model
3. Export model as `.pkl` or integrate directly

### 🔹 Android App
1. Open project in **Android Studio**
2. Build and run using emulator or physical device
3. Input nutritional data and get predicted calories

## 🤝 Credits
Developed by **Davin Williem**  
Politeknik Caltex Riau – 2024

## 📜 License
This project is for educational purposes only. Free to use and modify under MIT License.
