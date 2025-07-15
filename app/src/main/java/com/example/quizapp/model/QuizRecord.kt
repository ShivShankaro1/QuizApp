package com.example.quizapp.model

data class QuizRecord(
    val quizTitle: String,           // e.g., "Sports - Easy"
    val score: Int,                  // e.g., 5
    val total: Int,                  // e.g., 6
    val answers: List<AnswerRecord>  // All question/answer details
)

data class AnswerRecord(
    val question: String,            // Actual question text
    val userAnswer: String,          // What user selected
    val correctAnswer: String,       // Correct answer
    val correct: Boolean             // Whether user's answer was correct
)
