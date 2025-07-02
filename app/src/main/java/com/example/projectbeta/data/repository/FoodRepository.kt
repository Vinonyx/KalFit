package com.example.projectbeta.data.repository

import android.content.Context
import android.util.Log
import com.example.projectbeta.data.local.dao.FoodDao
import com.example.projectbeta.data.local.entity.FoodEntity
import kotlinx.coroutines.flow.Flow
import java.io.BufferedReader
import java.io.InputStreamReader

class FoodRepository(private val foodDao: FoodDao) {
    val allFoods: Flow<List<FoodEntity>> = foodDao.getAllFoods()

    suspend fun loadFoodsFromCsv(context: Context) {
        try {
            val inputStream = context.assets.open("foods.csv")
            Log.d("FoodRepository", "CSV file opened successfully")

            val reader = BufferedReader(InputStreamReader(inputStream))
            val foods = mutableListOf<FoodEntity>()

            // Skip header
            reader.readLine()

            reader.forEachLine { line ->
                try {
                    val tokens = line.split(",")
                    val food = FoodEntity(
                        id = tokens[0].toInt(),
                        calories = tokens[1].toInt(),
                        proteins = tokens[2].toFloat(),
                        fat = tokens[3].toFloat(),
                        carbohydrate = tokens[4].toFloat(),
                        name = tokens[5],
                        image = tokens[6]
                    )
                    foods.add(food)
//                    Log.d("FoodRepository", "Parsed food: ${food.name}")
                } catch (e: Exception) {
                    Log.e("FoodRepository", "Error parsing line: $line", e)
                }
            }

            Log.d("FoodRepository", "Inserting ${foods.size} foods into database")
            foodDao.insertAll(foods)
            Log.d("FoodRepository", "Foods inserted successfully")

        } catch (e: Exception) {
            Log.e("FoodRepository", "Error loading CSV", e)
        }
    }
}