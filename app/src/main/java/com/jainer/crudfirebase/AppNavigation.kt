package com.jainer.crudfirebase

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.loginScreen
    ) {
        composable(AppRoutes.loginScreen) {
            LoginScreen(navController = navController, modifier = modifier)
        }

        composable(AppRoutes.signupScreen) {
            SignupScreen(navController = navController, modifier = modifier)
        }

        composable(AppRoutes.homeScreen) {
            HomeScreen(navController = navController, modifier = modifier)
        }
    }
}
