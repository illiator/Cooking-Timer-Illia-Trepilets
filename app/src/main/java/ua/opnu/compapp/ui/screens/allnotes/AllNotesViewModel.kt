package ua.opnu.compapp.ui.screens.allnotes

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ua.opnu.compapp.MyApp
import ua.opnu.compapp.data.model.Note
import java.time.LocalDateTime

class AllNotesViewModel : ViewModel() {
    private val repository = MyApp.instance.repository

    val notes: Flow<List<Note>> = repository.notes

    // Додавання нотатки з таймером
    fun addNoteWithTimer(title: String, timerDuration: Int) {
        val newNote = Note(
            title = title,
            contents = "Timer: ${timerDuration / 60}:${timerDuration % 60}",
            isFavorite = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            timerDuration = timerDuration
        )
        CoroutineScope(Dispatchers.IO).launch {
            repository.addNote(newNote)
        }
    }

    fun deleteNoteById(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteNoteById(id)
        }
    }

}