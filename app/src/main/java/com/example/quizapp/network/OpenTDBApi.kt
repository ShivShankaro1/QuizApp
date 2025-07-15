package com.example.quizapp.network

import com.example.quizapp.model.QuizResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenTDBApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int,
        @Query("difficulty") difficulty: String,
        @Query("type") type: String = "multiple"
    ): QuizResponse
}
