package com.example.noteappwithkmm.presentation.home_notes_screen

import com.example.noteappwithkmm.common.utils.GenericMessageInfo
import com.example.noteappwithkmm.common.utils.Queue
import com.example.noteappwithkmm.domain.model.Note

data class HomeListNoteState(
    val isLoading: Boolean = false,
    val page: Int = 1,
    val query: String = "",
    val notes: List<Note> = listOf(),
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),

    )