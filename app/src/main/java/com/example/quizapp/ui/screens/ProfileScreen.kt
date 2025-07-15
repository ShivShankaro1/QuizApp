package com.example.quizapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quizapp.data.DataStoreManager
import com.example.quizapp.viewmodel.QuizViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: QuizViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStoreManager = remember { DataStoreManager(context) }

    var username by remember { mutableStateOf("guest_user") }
    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        username = dataStoreManager.getUsername() ?: "guest_user"
    }

    val quizRecords by viewModel.quizRecords.collectAsState()
    val recentRecords = remember(quizRecords) { quizRecords.takeLast(3).reversed() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("👤 Profile", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(8.dp))

            if (isEditing) {
                BasicTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    scope.launch { dataStoreManager.saveUsername(username) }
                    isEditing = false
                }) {
                    Text("Save")
                }
            } else {
                Text("Username: $username", modifier = Modifier.weight(1f))
                IconButton(onClick = { isEditing = true }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(Modifier.height(16.dp))
        Text("🧠 Total Quizzes: ${quizRecords.size}", style = MaterialTheme.typography.bodyLarge)

        Spacer(Modifier.height(16.dp))
        Text("🕒 Recent Quiz History", style = MaterialTheme.typography.titleMedium)

        if (recentRecords.isEmpty()) {
            Text("No recent quizzes.")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recentRecords.size) { index ->
                    val record = recentRecords[index]
                    var expanded by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("📘 ${record.quizTitle}", fontWeight = FontWeight.Bold)
                            Text("Score: ${record.score}/${record.total}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { expanded = !expanded },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(if (expanded) "Hide Answers" else "View Answers")
                            }

                            if (expanded) {
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

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("history") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("View Full History")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.clearQuizHistory() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset History")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                val summary = "User $username has completed ${quizRecords.size} quizzes."
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, summary)
                }
                context.startActivity(Intent.createChooser(intent, "Share via"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Share, contentDescription = "Share")
            Spacer(Modifier.width(8.dp))
            Text("Share Summary")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("about") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Info, contentDescription = "About")
            Spacer(Modifier.width(8.dp))
            Text("About App")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: Export to PDF or local file */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.FileDownload, contentDescription = "Export")
            Spacer(Modifier.width(8.dp))
            Text("Export Data")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* TODO: Navigate to Settings screen */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
            Spacer(Modifier.width(8.dp))
            Text("Settings")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* TODO: Show app usage tips */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Lightbulb, contentDescription = "Tips")
            Spacer(Modifier.width(8.dp))
            Text("Usage Tips")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* TODO: Contact support or open feedback form */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Email, contentDescription = "Feedback")
            Spacer(Modifier.width(8.dp))
            Text("Send Feedback")
        }

    }
}
