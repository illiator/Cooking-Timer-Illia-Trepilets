package ua.opnu.compapp.ui.screens.editnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.opnu.compapp.MyApp
import ua.opnu.compapp.data.model.Note
import java.time.LocalDateTime

class EditNoteViewModel : ViewModel() {

    private var _uiState = MutableStateFlow(EditNoteUiState())
    val uiState: StateFlow<EditNoteUiState> = _uiState

    private val repository = MyApp.instance.repository

    private var editNote: Note? = null

    fun setTitle(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun setText(text: String) {
        _uiState.update {
            it.copy(text = text)
        }
    }

    fun getNoteById(id: Long) {

        if (editNote != null)
            return

        if (id == -1L) {
            _uiState = MutableStateFlow(EditNoteUiState())
            return
        } else {

            viewModelScope.launch(Dispatchers.IO) {
                editNote = repository.getNoteById(id)
                editNote?.let {
                    setTitle(it.title)
                    setText(it.contents)
                }
            }
        }
    }

    fun addNote() {
        val date = LocalDateTime.now()
        val note = Note(
            title = _uiState.value.title,
            contents = _uiState.value.text,
            timerDuration = _uiState.value.timerDuration,
            isFavorite = false,
            createdAt = date,
            updatedAt = date
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.addNote(note)
        }

    }

    fun clearForm() {
        _uiState.update {
            it.copy(title = "", text = "")
        }
    }

    fun setTimerDuration(duration: Int) {
        _uiState.update {
            it.copy(timerDuration = duration)
        }
    }
}

data class EditNoteUiState(
    val title: String = "",
    val text: String = "",
    val timerDuration: Int = 0
)
