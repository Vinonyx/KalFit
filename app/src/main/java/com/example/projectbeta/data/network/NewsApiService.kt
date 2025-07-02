package com.example.projectbeta.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/everything")
    suspend fun getTopHeadlines(
        @Query("q") query: String = "healthy food",
        @Query("apiKey") apiKey: String
    ): NewsResponse
}
