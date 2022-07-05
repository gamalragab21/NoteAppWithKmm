package com.example.noteappwithkmm.presentation.add_note_screen

import com.example.noteappwithkmm.domain.model.Note
import com.example.noteappwithkmm.presentation.home_notes_screen.HomeListEvent

sealed class AddNoteEvent (){
    data class LoadNote(val noteId: String) : AddNoteEvent()

    data class OnQueryNoteTitleUpdate(val query: String) : AddNoteEvent()
    data class OnQueryNoteSubTitleUpdate(val query: String) : AddNoteEvent()
    data class OnQueryNoteBodyUpdate(val query: String) : AddNoteEvent()
    data class OnQueryNoteColorUpdate(val color: Int) : AddNoteEvent()
    object OnQueryNoteAddTime : AddNoteEvent()
    object OnQueryNoteUpdateTime : AddNoteEvent()
    data class DeleteNote(val note: Note) : AddNoteEvent()

    object SaveNote : AddNoteEvent()
    object OnRemoveHeaderMessageFromQueue : AddNoteEvent()

}