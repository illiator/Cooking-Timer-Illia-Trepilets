package ua.opnu.compapp.data

import kotlinx.coroutines.flow.Flow
import ua.opnu.compapp.data.database.NoteDao
import ua.opnu.compapp.data.model.Note

class NoteRepository(private val noteDao: NoteDao) {

    val notes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun addNote(note: Note) = noteDao.addNote(note)

    suspend fun getNoteById(id: Long) = noteDao.getNoteById(id)
    suspend fun updateNote(it: Note) = noteDao.updateNote(it)
    suspend fun deleteNoteById(id: Long) {noteDao.deleteNoteById(id)
    }


}