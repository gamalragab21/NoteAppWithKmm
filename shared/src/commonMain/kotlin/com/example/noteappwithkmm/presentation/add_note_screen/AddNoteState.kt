package com.example.noteappwithkmm.presentation.add_note_screen

import com.example.noteappwithkmm.common.utils.GenericMessageInfo
import com.example.noteappwithkmm.common.utils.Queue
import com.example.noteappwithkmm.domain.model.Note
import kotlinx.datetime.LocalDateTime

data class AddNoteState(
    val isLoading: Boolean = false,
    val note: Note ?= null,
    val queue: Queue<GenericMessageInfo> = Queue(mutableListOf()),
    )