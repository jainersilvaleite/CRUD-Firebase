package com.jainer.crudfirebase

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DatabaseReference

@Composable
fun AppNavigation(
    databaseReference: DatabaseReference,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.homeScreen
    ) {
        composable(AppRoutes.loginScreen) {
            LoginScreen(navController = navController, modifier = modifier)
        }

        composable(AppRoutes.signupScreen) {
            SignupScreen(navController = navController, modifier = modifier)
        }

        composable(AppRoutes.homeScreen) {
            HomeScreen(databaseReference = databaseReference, navController = navController, modifier = modifier)
        }
    }
}