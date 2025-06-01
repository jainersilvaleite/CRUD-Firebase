package com.jainer.crudfirebase

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

@Composable
fun HomeScreen(
    databaseReference: DatabaseReference,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Estado que armazena o texto para ser adicionado pelo usuário
    var text by rememberSaveable { mutableStateOf("") }
    // Estado que atualizará a lista de textos adicionados em tempo real
    var texts by rememberSaveable { mutableStateOf(emptyList<TextFirebase>()) }
    val context = LocalContext.current

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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.weight(1f)
        ) {
            texts.forEach { firebaseText ->
                Row(
                    modifier = modifier.fillMaxWidth().padding(10.dp)
                ) {
                    Text(text = firebaseText.content)
                    IconButton(
                        onClick = {
                            // UPDATE
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar texto"
                        )
                    }
                    IconButton(
                        onClick = {
                            // DELETE
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Deletar texto"
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
                    // Armazenamento (CREATE) do texto inserido no banco de dados
                    databaseReference.child(textId).setValue(firebaseText)
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Texto adicionado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
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
    }
}