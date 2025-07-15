package com.example.quizapp.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.quizapp.R
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    quizViewModel: QuizViewModel,
    onFinishQuiz: () -> Unit
) {
    val question = quizViewModel.currentQuestion
    val selectedOption by quizViewModel.selectedOption.collectAsState()
    val currentIndex by quizViewModel.currentIndex.collectAsState()
    val totalQuestions = quizViewModel.totalQuestions

    if (!quizViewModel.hasQuestions) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No questions available. Please try a different category.")
        }
        return
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.quiz_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Scrollable content
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lottie Animation
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .size(240.dp)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Question ${currentIndex + 1} of $totalQuestions",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Crossfade(targetState = question) { currentQuestion ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = currentQuestion.question,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                currentQuestion.options.forEachIndexed { index, option ->
                    Button(
                        onClick = { quizViewModel.selectOption(index) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedOption == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(option)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val quizFinished = quizViewModel.submitAnswer()
                        if (quizFinished) {
                            onFinishQuiz()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(if (quizViewModel.isLastQuestion) "Finish" else "Next")
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
