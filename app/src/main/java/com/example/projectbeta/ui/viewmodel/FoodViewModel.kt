package com.example.projectbeta.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectbeta.data.local.database.AppDatabase
import com.example.projectbeta.data.local.entity.FoodEntity
import com.example.projectbeta.data.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FoodViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FoodRepository
    val allFoods: Flow<List<FoodEntity>>

    init {
        val database = AppDatabase.getDatabase(application)
        val foodDao = database.foodDao()
        repository = FoodRepository(foodDao)
        allFoods = repository.allFoods

        // Tambahkan ini untuk memastikan data diload saat ViewModel dibuat
        viewModelScope.launch {
            repository.loadFoodsFromCsv(application)
        }
    }
}