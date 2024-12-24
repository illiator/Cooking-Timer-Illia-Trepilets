package ua.opnu.compapp.data.database

import androidx.room.TypeConverter
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
class LocalDateTimeConverter {

    @TypeConverter
    fun fromMillis(value: Long?) = value?.let {
        val instant = Instant.ofEpochMilli(it)
        instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    @TypeConverter
    fun toMillis(date: LocalDateTime?) = date?.let {
        it.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }
}

