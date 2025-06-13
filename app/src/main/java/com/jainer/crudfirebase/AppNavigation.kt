package com.jainer.crudfirebase

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

@Composable
fun AppNavigation(
    auth: FirebaseAuth,
    databaseReference: DatabaseReference,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (auth.currentUser != null) AppRoutes.homeScreen else AppRoutes.loginScreen
    ) {
        composable(AppRoutes.loginScreen) {
            LoginScreen(auth = auth, navController = navController, modifier = modifier)
        }

        composable(AppRoutes.signupScreen) {
            SignupScreen(auth = auth, navController = navController, modifier = modifier)
        }

        composable(AppRoutes.homeScreen) {
            HomeScreen(auth = auth, databaseReference = databaseReference, navController = navController, modifier = modifier)
        }
    }
}