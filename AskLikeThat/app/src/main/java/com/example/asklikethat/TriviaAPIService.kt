package com.example.asklikethat

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TriviaAPIService {
    private val service = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://opentdb.com")
        .build()
        .create(ITriviaAPIService::class.java)

    fun prepareCall(query: Query): Call<TriviaDTO> {
        return service.getQuestions(query.build())
    }
}