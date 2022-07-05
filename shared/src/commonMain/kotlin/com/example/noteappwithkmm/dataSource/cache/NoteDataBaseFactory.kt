package com.example.noteappwithkmm.dataSource.cache

import com.example.noteappwithkmm.common.utils.DataTimeUtil
import com.example.noteappwithkmm.domain.model.Note
import com.squareup.sqldelight.db.SqlDriver

class NoteDataBaseFactory constructor(
    private val driverFactory: DriverFactory,
) {
    fun createDataBase(): NoteDatabase {
        return NoteDatabase.invoke(driverFactory.createDriver())
    }

}

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun Note_Entity.toNote(dataTimeUtil: DataTimeUtil): Note {
    return Note(
        noteId = id.toInt(),
        noteSubTitle = noteSubTitle,
        noteTitle = noteTitle,
        noteBody = noteBody,
        noteTimeAdded = dataTimeUtil.toLocalDate(date_added),
        noteTimeUpdated = dataTimeUtil.toLocalDate(date_updated),
        noteImage = noteImage,
        noteUrl = noteUrl,
        noteColor = noteColor.toInt()
    )
}

fun List<Note_Entity>.toNoteList(dataTimeUtil: DataTimeUtil) = map {
    it.toNote(dataTimeUtil)
}