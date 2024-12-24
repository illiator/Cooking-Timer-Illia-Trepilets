package ua.opnu.compapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val contents: String,
    val isFavorite: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val timerDuration: Int? = null
)