package com.jainer.crudfirebase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun EditTextDialog(
    textFirebase: TextFirebase,
    onEdit: (TextFirebase) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estado que armazena o novo conteúdo do texto
    var content by rememberSaveable { mutableStateOf(textFirebase.content) }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth().fillMaxHeight(0.25f)
                .background(color = Color.White)
        ) {
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                placeholder = { Text(text = "Insira o novo conteúdo do texto") },
                modifier = modifier.fillMaxWidth()
            )
            // Botão para confirmar a edição do respectivo texto
            OutlinedButton(
                onClick = {
                    // Executa a função de edição do texto de acordo com o novo conteúdo inserido
                    onEdit(TextFirebase(id = textFirebase.id, content = content))
                    onDismissRequest()
                },
                modifier = modifier.fillMaxWidth()
            ) {
                Text(text = "Confirmar edição")
            }
        }
    }
}