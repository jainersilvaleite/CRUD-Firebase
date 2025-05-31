package com.jainer.crudfirebase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jainer.crudfirebase.ui.theme.CRUDFirebaseTheme

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize().background(color = Color.White)
    ) {
        Text(
            text = "FAZER LOGIN",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = modifier.height(20.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(text = "Insira seu e-mail") }
        )
        Spacer(modifier = modifier.height(20.dp))
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            placeholder = { Text(text = "Insira sua senha") }
        )
        Spacer(modifier = modifier.height(20.dp))
        OutlinedButton(
            onClick = {
                // CÓDIGO DE LOGIN NA CONTA
                navController.navigate(AppRoutes.homeScreen)
            }
        ) {
            Text(text = "Entrar na conta")
        }
        Spacer(modifier = modifier.height(10.dp))
        OutlinedButton(
            onClick = { navController.navigate(AppRoutes.signupScreen) }
        ) {
            Text(text = "Não tenho conta")
        }
    }
}

@Preview
@Composable
private fun LoginScreen() {
    CRUDFirebaseTheme {
        LoginScreen(rememberNavController())
    }
}