package com.example.asklikethat.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ITriviaAPIService {
    @GET("/api.php")
    fun getQuestions(@QueryMap filters: Map<String, String>): Call<TriviaDTO>
}