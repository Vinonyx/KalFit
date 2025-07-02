package com.example.projectbeta.data.model

import com.example.projectbeta.data.local.entity.FoodEntity

data class MealRecommendation(
    val meal: String,
    val foods: List<FoodEntity>
)