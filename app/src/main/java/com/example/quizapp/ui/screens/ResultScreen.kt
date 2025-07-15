package com.example.quizapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun ResultScreen(
    quizViewModel: QuizViewModel,
    onRestart: () -> Unit
) {
    val score = quizViewModel.score.collectAsState().value
    val total = quizViewModel.totalQuestions

    // Save quiz result once
    LaunchedEffect(Unit) {
        quizViewModel.saveCurrentQuizRecord()
    }

    val performanceText = when {
        score == total -> "Perfect! 🎯"
        score > total / 2 -> "Well done! ✅"
        else -> "Keep practicing! 💪"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = performanceText,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your Score",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "$score / $total",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onRestart) {
            Text("Restart Quiz")
        }
    }
}
