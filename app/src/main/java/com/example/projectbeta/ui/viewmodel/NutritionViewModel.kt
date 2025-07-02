package com.example.projectbeta.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projectbeta.data.local.entity.FoodEntity
import com.example.projectbeta.data.model.FoodRecommendation
import com.example.projectbeta.data.network.NutritionResponse
import com.example.projectbeta.data.repository.FoodRepository
import com.example.projectbeta.data.local.database.AppDatabase
import com.example.projectbeta.data.model.MealRecommendation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.round

class NutritionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FoodRepository
    private val _nutritionResponse = MutableLiveData<NutritionResponse>()
    val nutritionResponse: LiveData<NutritionResponse> = _nutritionResponse

    private val _foodEntityRecommendations = MutableLiveData<List<MealRecommendation>>()
    val foodEntityRecommendations: LiveData<List<MealRecommendation>> = _foodEntityRecommendations


    init {
        val database = AppDatabase.getDatabase(application)
        repository = FoodRepository(database.foodDao())
    }

    fun predictCalories(weight: Double, height: Double, age: Int, goal: String) {
        if (weight <= 0 || height <= 0 || age <= 0) return

        val bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)

        val targetCalories = when (goal) {
            "weight_loss" -> bmr * 0.85
            "muscle_gain" -> bmr * 1.15
            else -> bmr
        }

        val protein = weight * 2.0
        val fat = (targetCalories * 0.25) / 9
        val carbs = (targetCalories - (protein * 4) - (fat * 9)) / 4

        _nutritionResponse.value = NutritionResponse(
            protein = round(protein * 100) / 100,
            fat = round(fat * 100) / 100,
            carbohydrate = round(carbs * 100) / 100
        )
    }

    fun recommendFood(targetCalories: Double) {
        viewModelScope.launch {
            try {
                val foodDatabase = repository.allFoods.first()

                val dailyMeals = mapOf(
                    "Breakfast" to 0.3,
                    "Lunch" to 0.4,
                    "Dinner" to 0.3
                )

                val tolerance = 0.2
                val usedFoods = mutableSetOf<FoodEntity>()

                val recommendations = dailyMeals.map { (meal, proportion) ->
                    val mealCalories = targetCalories * proportion

                    val suitableFoods = foodDatabase.filter {
                        it !in usedFoods &&
                                it.calories.toDouble() in (mealCalories * (1 - tolerance))..(mealCalories * (1 + tolerance))
                    }

                    val foods = if (suitableFoods.isNotEmpty()) {
                        suitableFoods.take(1)
                    } else {
                        foodDatabase.filter { it !in usedFoods }
                            .sortedBy { abs(it.calories.toDouble() - mealCalories) }
                            .take(1)
                    }

                    usedFoods.addAll(foods)

                    // Langsung mengembalikan MealRecommendation dengan FoodEntity
                    MealRecommendation(meal, foods)
                }

                _foodEntityRecommendations.postValue(recommendations)

            } catch (e: Exception) {
                Log.e("NutritionViewModel", "Error in recommending food: ${e.message}")
            }
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NutritionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NutritionViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}