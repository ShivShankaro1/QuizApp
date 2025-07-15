// BottomNavBar.kt
package com.example.quizapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(val item: NavigationItem, val icon: ImageVector)

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(NavigationItem.Home, Icons.Default.Home),
        BottomNavItem(NavigationItem.Search, Icons.Default.Search),
        BottomNavItem(NavigationItem.Profile, Icons.Default.Person)
    )

    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.item.route,
                onClick = {
                    if (currentRoute != navItem.item.route) {
                        navController.navigate(navItem.item.route)
                    }
                },
                icon = { Icon(navItem.icon, contentDescription = navItem.item.title) },
                label = { Text(navItem.item.title) }
            )
        }
    }
}
