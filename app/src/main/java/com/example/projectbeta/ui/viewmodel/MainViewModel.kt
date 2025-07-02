package com.example.projectbeta.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.projectbeta.data.model.FoodItem

class MainViewModel : ViewModel() {
    val totalCalories = MutableLiveData<Double>()
    val macronutrients = MutableLiveData<Map<String, Double>>() // {"Protein": 75, "Fat": 44, "Carbs": 325}
    val foodRecommendations = MutableLiveData<List<FoodItem>>() // List of recommended foods

    fun loadData() {
        // Simulasi pengambilan data (Anda bisa mengganti ini dengan API call atau database query)
        totalCalories.value = 2000.0
        macronutrients.value = mapOf("Protein" to 75.0, "Fat" to 44.0, "Carbs" to 325.0)
        foodRecommendations.value = listOf(
            FoodItem("Avocado", 160.0, 2.0, 15.0, 9.0),
            FoodItem("Broccoli", 55.0, 3.7, 0.6, 11.0),
            FoodItem("Apple", 52.0, 0.3, 0.2, 14.0)
        )
    }
}
