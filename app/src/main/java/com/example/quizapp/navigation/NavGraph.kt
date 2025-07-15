package com.example.quizapp.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.quizapp.ui.screens.*
import com.example.quizapp.viewmodel.QuizViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun NavGraph(
    navController: NavHostController,
    quizViewModel: QuizViewModel,
    modifier: Modifier = Modifier,
    updateRoute: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            updateRoute("home")
            HomeScreen(
                onStartQuiz = { _, _ ->
                    navController.navigate("startquiz")
                }
            )
        }

        composable("startquiz") {
            updateRoute("startquiz")
            StartQuizScreen(
                onStart = { categoryId, difficulty ->
                    quizViewModel.fetchQuestions(categoryId, difficulty)
                    navController.navigate("quiz/$categoryId/$difficulty")
                }
            )
        }

        composable(
            route = "quiz/{categoryId}/{difficulty}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType },
                navArgument("difficulty") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            updateRoute("quiz")
            val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 9
            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: "easy"

            LaunchedEffect(Unit) {
                quizViewModel.fetchQuestions(categoryId, difficulty)
            }

            val isLoading by quizViewModel.isLoading.collectAsState()

            if (isLoading) {
                LoadingScreen()
            } else {
                QuizScreen(
                    quizViewModel = quizViewModel,
                    onFinishQuiz = {
                        navController.navigate("result")
                    }
                )
            }
        }

        composable("result") {
            updateRoute("result")
            ResultScreen(
                quizViewModel = quizViewModel,
                onRestart = {
                    quizViewModel.resetQuiz()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("about") {
            updateRoute("about")
            AboutScreen(navController = navController)
        }

        composable("search") {
            updateRoute("search")
            SearchScreen()
        }

        // ✅ Updated to pass navController
        composable("profile") {
            updateRoute("profile")
            ProfileScreen(
                viewModel = quizViewModel,
                navController = navController
            )
        }

        // ✅ NEW: History screen route
        composable("history") {
            updateRoute("history")
            HistoryScreen(
                viewModel = quizViewModel,
                navController = navController
            )
        }
    }
}
