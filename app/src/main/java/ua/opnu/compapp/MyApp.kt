package ua.opnu.compapp

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import com.github.javafaker.Faker
import ua.opnu.compapp.data.NoteRepository
import ua.opnu.compapp.data.database.NotesDatabase
import ua.opnu.compapp.data.model.Note
import java.time.LocalDateTime
import java.util.Random

class MyApp : Application() {

    private val database by lazy {
        NotesDatabase.getDatabase(this)
    }

    val repository by lazy {
        NoteRepository(database.noteDao())
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        // populateFakeData()
    }

    // region fake data

//    fun changeFavorite(note: Note) {
//        data.replaceAll { if (it.id == note.id) note.copy(isFavorite = note.isFavorite.not()) else it }
//    }

//    private fun populateFakeData() {
//        val faker = Faker(Random())
//
//        repeat(5) {
//            val note = Note(
//                it.toLong(),
//                faker.book().title(),
//                faker.chuckNorris().fact(),
//                false,
//                LocalDateTime.now(),
//                LocalDateTime.now()
//            )
//            data.add(note)
//        }
//    }

    /*
    private var lastDeletedNote: Note? = null

    fun deleteNote(note: Note) {
        lastDeletedNote = note
        data.remove(note)
    }

    fun undoDeletion() {
        lastDeletedNote?.let {
            data.add(it)
            data.sortBy { note -> note.updatedAt }
        }
    }
    */

    // endregion

    companion object {

        lateinit var instance: MyApp
            private set

        // val data = mutableStateListOf<Note>()
    }

}