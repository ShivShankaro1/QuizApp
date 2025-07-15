package com.example.quizapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.navigation.NavGraph
import com.example.quizapp.viewmodel.QuizViewModel

@Composable
fun MainScreen(quizViewModel: QuizViewModel = viewModel()) {
    val navController = rememberNavController()

    // Only these screens should show in the bottom nav bar
    val bottomNavItems = listOf("home", "search", "profile")

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavItems) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen,
                            onClick = {
                                if (currentRoute != screen) {
                                    navController.navigate(screen) {
                                        launchSingleTop = true
                                        restoreState = true
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = when (screen) {
                                        "home" -> Icons.Default.Home
                                        "search" -> Icons.Default.Search
                                        "profile" -> Icons.Default.Person
                                        else -> Icons.Default.Help
                                    },
                                    contentDescription = screen
                                )
                            },
                            label = {
                                Text(screen.replaceFirstChar { it.uppercase() })
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            quizViewModel = quizViewModel,
            modifier = Modifier.padding(innerPadding),
            updateRoute = { /* optional tracking */ }
        )
    }
}
