package com.example.projectbeta.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val calories: Int,
    val proteins: Float,
    val fat: Float,
    val carbohydrate: Float,
    val image: String
)
