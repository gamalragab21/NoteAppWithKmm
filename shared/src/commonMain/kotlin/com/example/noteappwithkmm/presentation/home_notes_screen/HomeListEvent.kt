package com.example.noteappwithkmm.presentation.home_notes_screen

import com.example.noteappwithkmm.domain.model.Note

sealed class HomeListEvent (){
    object LoadNotes : HomeListEvent()
    object NextPage : HomeListEvent()
    data class OnQueryUpdate(val query: String) : HomeListEvent()
    data class DeleteNote(val note: Note) : HomeListEvent()
    object ExecuteNewSearch : HomeListEvent()
    object OnRemoveHeaderMessageFromQueue : HomeListEvent()

}