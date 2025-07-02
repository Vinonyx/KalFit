package com.example.projectbeta.data.model

data class FoodRecommendation(
    val meal: String, // e.g., "Breakfast", "Lunch", "Dinner"
    val recommendedFoods: List<FoodItem>
)