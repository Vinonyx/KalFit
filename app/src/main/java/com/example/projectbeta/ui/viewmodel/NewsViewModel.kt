package com.example.projectbeta.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.projectbeta.data.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers

class NewsViewModel : ViewModel() {
    private val apiKey = "03e674a93ec0442aa4136257c9b5cbfd" // Ganti dengan API Key dari NewsAPI

    val healthyFoodNews = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitInstance.api.getTopHeadlines(
                query = "healthy food", // Tambahkan kata kunci
                apiKey = apiKey
            )
            if (response.articles.isNotEmpty()) {
                emit(response.articles)
            } else {
                // Log jika tidak ada artikel
                emit(emptyList())
                println("No articles found for the specified query.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
