package com.example.projectbeta.data.model

data class PredictionData(
    val protein: Float = 0f,
    val fat: Float = 0f,
    val carbohydrate: Float = 0f,
    val predictedCalories: Float = 0f
)