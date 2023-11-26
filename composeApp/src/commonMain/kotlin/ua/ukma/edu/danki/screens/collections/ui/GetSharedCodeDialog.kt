package ua.ukma.edu.danki.screens.collections.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GetSharedCodeDialog(
    onSubmit: (Long) -> Unit
) {
    var code: String by mutableStateOf("")
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(24.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier.padding(8.dp).fillMaxWidth(0.9f),
                value = code,
                onValueChange = {
                    code = it
                },
                placeholder = { Text("Create invite code to share collection") },
                shape = MaterialTheme.shapes.extraLarge,
                singleLine = true,
            )
            TextButton(onClick = {
                code.toLongOrNull()?.let {
                    onSubmit(it)
                }
            }) {
                Text(
                    text = "Get",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

    }
}