package com.example.quizapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.model.QuizRecord
import com.example.quizapp.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, viewModel: QuizViewModel) {
    val historyList by viewModel.quizRecords.collectAsState()

    // Track which quiz records are expanded
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz History") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No quiz history yet.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(historyList) { index, record ->
                    val isExpanded = expandedStates[index] ?: false

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("📝 ${record.quizTitle}", fontWeight = FontWeight.Bold)
                            Text("Score: ${record.score}/${record.total}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    expandedStates[index] = !isExpanded
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(if (isExpanded) "Hide Answers" else "View Answers")
                            }

                            if (isExpanded) {
                                Spacer(modifier = Modifier.height(8.dp))

                                record.answers.forEach { answer ->
                                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                                        Text("Q: ${answer.question}", fontWeight = FontWeight.SemiBold)
                                        Text("Your Answer: ${answer.userAnswer}")
                                        if (!answer.correct) {
                                            Text(
                                                "Correct Answer: ${answer.correctAnswer}",
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                        Text(
                                            text = if (answer.correct) "✅ Correct" else "❌ Incorrect",
                                            color = if (answer.correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
