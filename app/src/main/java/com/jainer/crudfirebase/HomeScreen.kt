package com.jainer.crudfirebase

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

@Composable
fun HomeScreen(
    auth: FirebaseAuth,
    databaseReference: DatabaseReference,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Estado que armazena o texto para ser adicionado pelo usuário
    var text by rememberSaveable { mutableStateOf("") }
    // Estado que atualizará a lista de textos adicionados em tempo real
    var texts by rememberSaveable { mutableStateOf(emptyList<TextFirebase>()) }
    // Estado responsável por recarregar a página com os textos sempre que necessário
    var refreshObserver by remember { mutableStateOf(false) }
    // Estado que manterá o diálogo de edição aberto, se necessário
    var isEditTextDialogOpened by rememberSaveable { mutableStateOf(false) }

    // Contexto local para emissão de mensagens Toast
    val context = LocalContext.current

    LaunchedEffect(refreshObserver) {
        // Limpa a lista de textos para evitar incoerências
        texts = emptyList()

        // Coletando (READ) todos os textos adicionados no banco para associá-las ao estado
        val textsList = mutableListOf<TextFirebase>()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val textSnapshot = it.getValue(TextFirebase::class.java)

                    if (textSnapshot != null) {
                        textsList.add(textSnapshot)
                    }
                }
                texts = textsList
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    context,
                    "Ocorreu um erro ao adicionar o texto: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // Coluna onde todos os elementos da tela estarão dispostos
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.weight(1f).padding(20.dp)
        ) {
            item {
                // Loop em cada texto coletado do banco de dados para exibição em tela
                texts.forEach { firebaseText ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier.fillMaxWidth().padding(10.dp)
                    ) {
                        Text(text = firebaseText.content)
                        // Botão para editar o respectivo texto
                        IconButton(
                            onClick = {
                                isEditTextDialogOpened = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar texto"
                            )
                        }
                        IconButton(
                            onClick = {
                                // Remoção (DELETE) do valor respectivo ao id de um texto armazenado
                                databaseReference.child(firebaseText.id).removeValue()
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Texto removido com sucesso!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        // Atualiza a página para exibir os textos atualizados
                                        refreshObserver = !refreshObserver
                                    }
                                    .addOnFailureListener { failure ->
                                        Toast.makeText(
                                            context,
                                            "Deu erro: ${failure.message}!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Deletar texto"
                            )
                        }
                    }

                    // Diálogo para edição do texto selecionado
                    if (isEditTextDialogOpened) {
                        EditTextDialog(
                            textFirebase = firebaseText,
                            onEdit = { newFirebaseText ->
                                val textMap = mapOf("content" to newFirebaseText.content)

                                // Edição (UPDATE) do conteúdo do respectivo texto com base no textMap
                                databaseReference.child(newFirebaseText.id).updateChildren(textMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "Texto editado com sucesso!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        // Atualiza a página para exibir os textos atualizados
                                        refreshObserver = !refreshObserver
                                    }
                                    .addOnFailureListener { failure ->
                                        Toast.makeText(
                                            context,
                                            "Deu erro: ${failure.message}!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            },
                            onDismissRequest = {
                                isEditTextDialogOpened = false
                            }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(text = "Insira um texto para adicionar") },
            modifier = modifier.fillMaxWidth()
        )
        OutlinedButton(
            onClick = {
                // Geração do id do texto a ser adicionado
                val textId = databaseReference.push().key

                if (textId != null) {
                    val firebaseText = TextFirebase(id = textId, content = text)
                    texts = texts + firebaseText // atualiza na interface para melhor experiência

                    // Armazenamento (CREATE) do texto inserido no banco de dados
                    databaseReference.child(textId).setValue(firebaseText)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Texto adicionado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Atualiza a página para exibir os textos atualizados
                            refreshObserver = !refreshObserver
                        }
                        .addOnFailureListener { failure ->
                            Toast.makeText(
                                context,
                                "Deu erro: ${failure.message}!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }

            },
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = "Adicionar texto")
        }
        OutlinedButton(
            onClick = {
                // Saindo da conta atualmente logada
                auth.signOut()
                navController.navigate(route = AppRoutes.loginScreen) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = "Sair da conta")
        }
    }
}