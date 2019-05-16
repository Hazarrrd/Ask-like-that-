package com.example.asklikethat.api

import com.example.asklikethat.Question

data class TriviaDTO(val response_code: Int, val results: ArrayList<Question>)