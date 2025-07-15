package com.example.quizapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.ui.screens.HomeScreen
import com.example.quizapp.ui.screens.QuizScreen
import com.example.quizapp.ui.screens.ResultScreen
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun QuizApp() {
    val navController = rememberNavController()
    val quizViewModel: QuizViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onStartQuiz = { categoryId: Int, difficulty: String ->
                navController.navigate("quiz/$categoryId/$difficulty")
            })
        }

        composable("quiz") {
            QuizScreen(
                quizViewModel = quizViewModel,
                onFinishQuiz = {
                    navController.navigate("result")
                }
            )
        }

        composable("result") {
            ResultScreen(
                quizViewModel = quizViewModel,
                onRestart = {
                    quizViewModel.resetQuiz()
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }
    }
}
