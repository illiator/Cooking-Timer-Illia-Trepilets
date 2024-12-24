package ua.opnu.compapp.ui.screens.editnote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ua.opnu.compapp.ui.theme.AppTypography

@Composable
fun EditNoteScreen(
    id: Long,
    navController: NavController,
    viewModel: EditNoteViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val createMode = id == -1L
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(id) {
        if (!createMode) viewModel.getNoteById(id)
    }

    var timerInput by remember { mutableStateOf(formatTime(uiState.timerDuration)) }
    var timerError by remember { mutableStateOf<String?>(null) }

    fun validateTimer(): Boolean {
        val duration = parseTime(timerInput)
        return if (duration == null || duration == 0) {
            timerError = "Time must be greater than 00:00."
            false
        } else {
            timerError = null
            viewModel.setTimerDuration(duration)
            true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (createMode) "Add Note" else "Edit Note",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )

        OutlinedTextField(
            value = uiState.title,
            onValueChange = viewModel::setTitle,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        OutlinedTextField(
            value = uiState.text,
            onValueChange = viewModel::setText,
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        OutlinedTextField(
            value = timerInput,
            onValueChange = { timerInput = it },
            label = { Text("Timer (MM:SS)") },
            isError = timerError != null,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        // Показуємо помилку, якщо вона є
        timerError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (validateTimer() && uiState.title.isNotBlank()) {
                        viewModel.addNote()
                        navController.navigateUp()
                    } else if (uiState.title.isBlank()) {
                        timerError = "Title cannot be empty."
                    }
                }
            ) {
                Text(if (createMode) "Add Note" else "Update Note")
            }
            Button(onClick = { viewModel.clearForm() }) {
                Text("Clear")
            }
        }
    }
}

fun parseTime(input: String): Int? {
    return try {
        val parts = input.split(":").map { it.toInt() }
        if (parts.size == 2 && parts[0] >= 0 && parts[1] in 0..59) {
            parts[0] * 60 + parts[1]
        } else null
    } catch (e: NumberFormatException) {
        null
    }
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}
