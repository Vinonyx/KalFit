package com.example.projectbeta.data.network

import com.example.projectbeta.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

//interface ApiService {
//    @GET("top-headlines")
//    suspend fun getUsers(
//        @Query("sources") sources: String = "bbc-news",
//        @Query("apiKey") apiKey: String = "03e674a93ec0442aa4136257c9b5cbfd"
//    ): NewsResponse
//}

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>

//    @POST("product")
//    suspend fun createProduct(
//        @Header("Authorization") token: String,
//        @Body products: List<ProductRequest>,
//    ): ProductResponse
//
//    @GET("product")
//    suspend fun getProducts(
//        @Header("Authorization") token: String
//    ): ProductResponse

}