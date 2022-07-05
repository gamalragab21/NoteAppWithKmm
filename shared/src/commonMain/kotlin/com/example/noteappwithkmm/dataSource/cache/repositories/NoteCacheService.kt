package com.example.noteappwithkmm.dataSource.cache.repositories

import com.example.noteappwithkmm.domain.model.Note

interface NoteCacheService {
   suspend fun getAllNotes(page: Int):List<Note>
   suspend fun getAllNoteByTitleOrSubTitle(query: String, page: Int):List<Note>
   suspend fun insertNote(note:Note)
//   suspend fun updateNote(note: Note)
   suspend fun deleteNote(note:Note)
   suspend fun getNoteById(recipeId: Int): Note?
   suspend fun clearDatabase()
}