package ua.opnu.compapp.ui.screens.allnotes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ua.opnu.compapp.data.model.Note
import ua.opnu.compapp.ui.navigation.Graph


@Composable
fun AllNotesScreen(navController: NavController, notes: List<Note>, viewModel: AllNotesViewModel = viewModel()) {
    var selectedNoteTitle by remember { mutableStateOf("Default Note") }
    var selectedNoteId by remember { mutableStateOf<Long?>(null) }
    var timerDuration by remember { mutableStateOf(60) }
    var timeLeft by remember { mutableStateOf(timerDuration) }
    var timerRunning by remember { mutableStateOf(false) }
    var countDownTimer: CountDownTimer? by remember { mutableStateOf(null) }

    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }


    // Форматування часу у формат MM:SS
    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    // Запуск таймера 
    fun startTimer() {
        if (timerRunning) return
        timerRunning = true
        countDownTimer = object : CountDownTimer(timeLeft * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onFinish() {
                timerRunning = false
                timeLeft = timerDuration
                // Вібросигнал
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))

            }
        }.start()
    }

    // Зупинка таймера
    fun stopTimer() {
        countDownTimer?.cancel()
        timerRunning = false
    }

    // Видалення вибраної нотатки
    fun deleteSelectedNote() {
        selectedNoteId?.let {
            viewModel.deleteNoteById(it)}
        selectedNoteTitle = "Default Note"
        selectedNoteId = null
        timerDuration = 60
        timeLeft = timerDuration
        stopTimer()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Назва обраної нотатки
            Text(
                text = selectedNoteTitle,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Таймер
            Text(
                text = "Timer: ${formatTime(timeLeft)}",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,

                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Button(onClick = { startTimer() }, enabled = !timerRunning) {
                    Text(text = "Start Timer")
                }
                Button(onClick = { stopTimer() }, enabled = timerRunning) {
                    Text(text = "Stop Timer")
                }
                Button(onClick = {
                    // Скидання значень таймера
                    timerDuration = timerDuration
                    timeLeft = timerDuration
                    timerRunning = false
                    countDownTimer?.cancel()
                }) {
                    Text(text = "Reset Timer")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Список нотаток
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedNoteTitle = note.title
                                selectedNoteId = note.id
                                timerDuration = note.timerDuration ?: 60
                                timeLeft = timerDuration
                                stopTimer()
                            },
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer // Колір фону
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // Текст нотатки
                            Column {
                                Text(
                                    text = note.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = note.contents,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                // Час, зазначений у нотатці
                                note.timerDuration?.let { duration ->
                                    Text(
                                        text = "Time: ${formatTime(duration)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = { deleteSelectedNote() },
                containerColor = MaterialTheme.colorScheme.error
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note")
            }

            FloatingActionButton(
                onClick = { navController.navigate("${Graph.EDIT_NOTE}/-1") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Add Note")
            }
        }
    }
}

