from flask import Flask, jsonify, request
import joblib 
import numpy as np
import pandas as pd

# Inisialisasi Flask app
app = Flask(__name__)

# Load model ML
try:
    model = joblib.load('nutrition_model.pkl')
except FileNotFoundError as e:
    raise FileNotFoundError(f"Model file tidak ditemukan: {str(e)}")

# Load database makanan (pastikan file ini ada)
try:
    food_database = pd.read_csv('nutrition.csv')  # Ganti dengan nama file database Anda
except FileNotFoundError:
    raise FileNotFoundError("Database file 'nutrition.csv' tidak ditemukan. Pastikan path benar.")

@app.route('/')
def home():
    return "Backend API is running!"

# Endpoint untuk prediksi kalori berdasarkan BMR
@app.route('/macronutrients_calculation', methods=['POST'])
def calculate_macronutrients():
    try:
        # Dapatkan data dari request
        data = request.get_json()

        # Pastikan semua fitur yang diperlukan ada dalam request
        required_features = ['weight', 'height', 'age', 'goal']
        if not all(feature in data for feature in required_features):
            return jsonify({'error': 'Invalid input. Required features: weight, height, age, goal.'}), 400

        # Ambil data dari request
        weight = data['weight']
        height = data['height']
        age = data['age']
        goal = data['goal']

        # Hitung BMR menggunakan formula Harris-Benedict
        bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)

        # Sesuaikan kebutuhan kalori berdasarkan tujuan
        if goal == 'weight_loss':
            target_calories = bmr * 0.85  # Defisit kalori 15%
        elif goal == 'muscle_gain':
            target_calories = bmr * 1.15  # Surplus kalori 15%
        else:  # maintenance
            target_calories = bmr

        # Hitung kebutuhan makronutrien
        protein = weight * 2.0  # 2g protein per kg berat badan
        fat = (target_calories * 0.25) / 9  # 25% dari total kalori
        carbs = (target_calories - (protein * 4) - (fat * 9)) / 4

        # Kembalikan hasil dalam format JSON
        return jsonify({
            'protein': round(protein, 2),
            'fat': round(fat, 2),
            'carbohydrate': round(carbs, 2)
        })

    except Exception as e:
        return jsonify({'error': str(e)}), 500

# Fungsi untuk rekomendasi makanan
def get_food_recommendations(target_nutrients, food_database, tolerance=0.2):
    daily_meals = {
        'sarapan': 0.3,  # 30% dari total kalori
        'makan_siang': 0.4,  # 40% dari total kalori
        'makan_malam': 0.3   # 30% dari total kalori
    }
    
    recommendations = {}
    
    for meal, proportion in daily_meals.items():
        # Hitung target nutrisi untuk setiap waktu makan
        meal_targets = {
            'calories': target_nutrients['target_calories'] * proportion,
            'protein': target_nutrients['protein'] * proportion,
            'fat': target_nutrients['fat'] * proportion,
            'carbohydrate': target_nutrients['carbohydrate'] * proportion
        }
        
        # Filter makanan yang sesuai dengan target (dalam range toleransi)
        suitable_foods = food_database[
            (food_database['calories'] >= meal_targets['calories'] * (1 - tolerance)) &
            (food_database['calories'] <= meal_targets['calories'] * (1 + tolerance)) &
            (food_database['proteins'] >= meal_targets['protein'] * (1 - tolerance)) &
            (food_database['proteins'] <= meal_targets['protein'] * (1 + tolerance))
        ]
        
        # Pilih 3 makanan random dari hasil filter
        if len(suitable_foods) >= 1:
            recommendations[meal] = suitable_foods.sample(n=3)[['name', 'calories', 'proteins', 'fat', 'carbohydrate']].to_dict('records')
        else:
            # Jika tidak cukup makanan yang sesuai, ambil yang paling mendekati
            recommendations[meal] = food_database.iloc[
                (abs(food_database['calories'] - meal_targets['calories'])).argsort()[:3]
            ][['name', 'calories', 'proteins', 'fat', 'carbohydrate']].to_dict('records')
    
    return recommendations

# Endpoint untuk rekomendasi makanan
@app.route('/recommend', methods=['POST'])
def recommend_food():
    try:
        # Data dari request
        data = request.get_json()

        # Pastikan input yang diperlukan ada dalam request
        required_features = ['target_calories', 'protein', 'fat', 'carbohydrate']
        if not all(feature in data for feature in required_features):
            return jsonify({'error': 'Invalid input. Required: target_calories, protein, fat, carbohydrate.'}), 400

        # Ambil data dari request
        target_nutrients = {
            'target_calories': data['target_calories'],
            'protein': data['protein'],
            'fat': data['fat'],
            'carbohydrate': data['carbohydrate']
        }

        # Terapkan toleransi nutrisi
        tolerance = data.get('tolerance', 0.2)  # Toleransi default 20%

        # Filter makanan berdasarkan nutrisi dan rekomendasi makanan
        recommendations = get_food_recommendations(target_nutrients, food_database, tolerance)

        # Kembalikan hasil rekomendasi dalam format JSON
        return jsonify(recommendations)

    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/predict_calories_by_nutrients', methods=['POST'])
def predict_calories_by_nutrients():
    """
    Endpoint untuk memprediksi kalori berdasarkan jumlah protein, fat, dan carbohydrate.
    """
    try:
        # Ambil data dari request
        data = request.get_json()

        # Pastikan input yang diperlukan tersedia
        required_features = ['proteins', 'fat', 'carbohydrate']
        if not all(feature in data for feature in required_features):
            return jsonify({'error': 'Invalid input. Required: proteins, fat, carbohydrate.'}), 400

        # Ambil nilai input
        proteins = data['proteins']
        fat = data['fat']
        carbohydrate = data['carbohydrate']

        # Pastikan input dalam bentuk array 2D untuk prediksi
        input_features = np.array([[proteins, fat, carbohydrate]])

        # Prediksi kalori menggunakan model
        predicted_calories = model.predict(input_features)

        # Kembalikan hasil prediksi
        return jsonify({'predicted_calories': round(predicted_calories[0], 2)})

    except Exception as e:
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True)
