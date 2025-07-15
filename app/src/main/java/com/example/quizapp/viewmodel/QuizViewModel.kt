package com.example.quizapp.viewmodel

import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.QuestionItem
import com.example.quizapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AnswerRecord(
    val question: String,
    val userAnswer: String,
    val correctAnswer: String,
    val correct: Boolean
)

data class QuizRecord(
    val quizTitle: String,
    val score: Int,
    val total: Int,
    val answers: List<AnswerRecord>
)

class QuizViewModel : ViewModel() {

    private val _questions = MutableStateFlow<List<QuestionItem>>(emptyList())
    private val _currentIndex = MutableStateFlow(0)
    private val _selectedOption = MutableStateFlow(-1)
    private val _score = MutableStateFlow(0)
    private val _quizRecords = MutableStateFlow<List<QuizRecord>>(emptyList())

    val currentIndex: StateFlow<Int> = _currentIndex
    val selectedOption: StateFlow<Int> = _selectedOption
    val score: StateFlow<Int> = _score
    val quizRecords: StateFlow<List<QuizRecord>> = _quizRecords

    private var currentAnswers = mutableListOf<AnswerRecord>()
    private var currentQuizTitle: String = "Untitled Quiz"

    var isLoading = MutableStateFlow(false)
        private set

    var loadingError = MutableStateFlow(false)
        private set

    val totalQuestions: Int
        get() = _questions.value.size

    val currentQuestion: QuestionItem
        get() = _questions.value.getOrElse(_currentIndex.value) {
            QuestionItem(
                question = "No question found",
                options = listOf("N/A"),
                answerIndex = 0
            )
        }

    val isLastQuestion: Boolean
        get() = _currentIndex.value == _questions.value.lastIndex

    val hasQuestions: Boolean
        get() = _questions.value.isNotEmpty()

    fun selectOption(index: Int) {
        _selectedOption.value = index
    }

    fun submitAnswer(): Boolean {
        val selectedIndex = _selectedOption.value
        val correctIndex = currentQuestion.answerIndex

        if (selectedIndex == correctIndex) {
            _score.value++
        }

        currentAnswers.add(
            AnswerRecord(
                question = currentQuestion.question,
                userAnswer = currentQuestion.options[selectedIndex],
                correctAnswer = currentQuestion.options[correctIndex],
                correct = selectedIndex == correctIndex
            )
        )

        val wasLast = isLastQuestion
        _selectedOption.value = -1
        if (!wasLast) {
            _currentIndex.value++
        }
        return wasLast
    }

    fun resetQuiz() {
        _currentIndex.value = 0
        _selectedOption.value = -1
        _score.value = 0
        _questions.value = emptyList()
        currentAnswers.clear()
        isLoading.value = false
        loadingError.value = false
    }

    fun fetchQuestions(category: Int, difficulty: String) {
        viewModelScope.launch {
            isLoading.value = true
            loadingError.value = false
            try {
                val response = RetrofitInstance.api.getQuestions(
                    amount = 6,
                    category = category,
                    difficulty = difficulty
                )

                if (response.responseCode == 0 && response.results.isNotEmpty()) {
                    val questionsList = response.results
                        .filter { it.question.length < 150 }
                        .map { result ->
                            val allOptions = (result.incorrectAnswers + result.correctAnswer).shuffled()
                            val decodedQuestion = HtmlCompat.fromHtml(result.question, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                            val decodedOptions = allOptions.map {
                                HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                            }
                            QuestionItem(
                                question = decodedQuestion,
                                options = decodedOptions,
                                answerIndex = decodedOptions.indexOf(
                                    HtmlCompat.fromHtml(result.correctAnswer, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                                )
                            )
                        }

                    _questions.value = questionsList
                    _currentIndex.value = 0
                    _selectedOption.value = -1
                    currentAnswers.clear()
                    currentQuizTitle = "${difficulty.uppercase()} Quiz - $category"
                } else {
                    _questions.value = emptyList()
                    loadingError.value = true
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _questions.value = emptyList()
                loadingError.value = true
            } finally {
                isLoading.value = false
            }
        }
    }

    fun saveCurrentQuizRecord() {
        val record = QuizRecord(
            quizTitle = currentQuizTitle,
            score = _score.value,
            total = _questions.value.size,
            answers = currentAnswers.toList()
        )
        _quizRecords.value = _quizRecords.value + record
    }

    fun clearQuizHistory() {
        _quizRecords.value = emptyList()
    }
}
