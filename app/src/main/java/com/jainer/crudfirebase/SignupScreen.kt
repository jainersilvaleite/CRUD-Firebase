package com.jainer.crudfirebase

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignupScreen(
    auth: FirebaseAuth,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize().background(color = Color.White)
    ) {
        Text(
            text = "FAZER CADASTRO",
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
                // Criação de um novo usuário (cadastro) a partir do email e senha informados
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Cadastro realizado com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()

                        auth.signOut()

                        if (auth.uid == null) {
                            navController.navigate(AppRoutes.loginScreen)
                        }
                    }
                    .addOnFailureListener { failure ->
                        Toast.makeText(
                            context,
                            "Ocorreu um erro ao fazer cadastro: ${failure.message}!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        ) {
            Text(text = "Criar conta")
        }
        Spacer(modifier = modifier.height(10.dp))
        OutlinedButton(
            onClick = {
                navController.navigate(route = AppRoutes.loginScreen) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        ) {
            Text(text = "Já tenho uma conta")
        }
    }
}