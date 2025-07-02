package com.example.projectbeta.data.network

data class NewsResponse(
    val articles: List<Article>
)

data class Article(
    val title: String,
    val description: String,
    val urlToImage: String,
    val url: String
)
