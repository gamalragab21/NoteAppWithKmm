package com.example.noteappwithkmm.domain.repositories

import com.example.noteappwithkmm.common.Constants.NOTE_PAGINATION_PAGE_SIZE
import com.example.noteappwithkmm.common.utils.DataTimeUtil
import com.example.noteappwithkmm.dataSource.cache.NoteDatabase
import com.example.noteappwithkmm.dataSource.cache.NoteDbQueries
import com.example.noteappwithkmm.dataSource.cache.repositories.NoteCacheService
import com.example.noteappwithkmm.dataSource.cache.toNote
import com.example.noteappwithkmm.dataSource.cache.toNoteList
import com.example.noteappwithkmm.domain.model.Note
import kotlinx.datetime.LocalDateTime

class NoteCacheServiceImpl(
    private val noteDatabase: NoteDatabase,
    private val dataTimeUtil: DataTimeUtil,
) :
    NoteCacheService {
    init {
        println("GAMALRAGAB init NoteCacheServiceImpl")
    }
    private val queries: NoteDbQueries = noteDatabase.noteDbQueries

    override suspend fun insertNote(note: Note) { // if id id not null it's will update it in db
        val noteTimeAdded =
            dataTimeUtil.toEpochMilliseconds(note.noteTimeAdded)
        val noteTimeUpdated =
            dataTimeUtil.toEpochMilliseconds(note.noteTimeAdded)
        
        queries.insertNote(
            id = note.noteId?.toLong(),
            noteUrl = note.noteUrl,
            noteImage = note.noteImage,
            noteBody = note.noteBody,
            noteTitle = note.noteTitle,
            noteSubTitle = note.noteSubTitle,
            date_updated = noteTimeUpdated,
            date_added = noteTimeAdded,
            noteColor=note.noteColor.toLong()
        )
        println("GAMALRAGAB insert success")

    }

    override suspend fun deleteNote(note: Note) {
        queries.removeNote(note.noteId!!.toLong())
    }

    override suspend fun getAllNotes(page: Int): List<Note> {

        return queries.getAllNotes(
            pageSize = NOTE_PAGINATION_PAGE_SIZE.toLong(),
            offset = ((page - 1) * NOTE_PAGINATION_PAGE_SIZE).toLong()
        ).executeAsList().toNoteList(dataTimeUtil)
    }

    override suspend fun getAllNoteByTitleOrSubTitle(query: String, page: Int): List<Note> {
        return queries.searchNotes(
            query = query,
            pageSize = NOTE_PAGINATION_PAGE_SIZE,
            offset = ((page - 1) * NOTE_PAGINATION_PAGE_SIZE)
        ).executeAsList().map {
            it.toNote(dataTimeUtil)
        }
    }

    override suspend  fun getNoteById(recipeId: Int): Note? {
        val recipe = queries.getNoteById(id = recipeId.toLong())
            .executeAsOneOrNull()?.toNote(dataTimeUtil)
        println("RecipeData from repo: $recipe")
        return recipe

    }

    override suspend fun clearDatabase() {
        queries.removeAllNotes()
    }

//    override suspend fun updateNote(note: Note) {
//        insertNote(note = note)
//    }
}