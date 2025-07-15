package com.example.quizapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.components.DropdownMenuBox

private val categories = listOf(
    "General Knowledge" to 9,
    "Sports" to 21,
    "Science & Nature" to 17,
    "Mathematics" to 19,
    "Health & Medicine" to 13,
    "Video Games" to 15,
    "Geography" to 22
)

private val difficulties = listOf("easy", "medium", "hard")

@Composable
fun StartQuizScreen(
    onStart: (categoryId: Int, difficulty: String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var selectedDifficulty by remember { mutableStateOf(difficulties[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Select Category", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        DropdownMenuBox(
            options = categories.map { it.first },
            selectedOption = selectedCategory.first
        ) { selected ->
            selectedCategory = categories.first { it.first == selected }
        }

        Spacer(Modifier.height(16.dp))
        Text("Select Difficulty", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        DropdownMenuBox(
            options = difficulties,
            selectedOption = selectedDifficulty
        ) { selected ->
            selectedDifficulty = selected
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                onStart(selectedCategory.second, selectedDifficulty)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Start Quiz")
        }
    }
}
