// NavigationItem.kt
package com.example.quizapp.navigation

sealed class NavigationItem(val route: String, val title: String) {
    object Home : NavigationItem("home", "Home")
    object Search : NavigationItem("search", "Search")
    object About : NavigationItem("about", "About")
    object Profile : NavigationItem("profile", "Profile")
}
