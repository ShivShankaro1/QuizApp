package com.example.quizapp.model

data class QuestionItem(
    val question: String,
    val options: List<String>,
    val answerIndex: Int
)
