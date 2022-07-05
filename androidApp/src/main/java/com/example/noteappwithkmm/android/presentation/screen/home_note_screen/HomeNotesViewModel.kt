package com.example.noteappwithkmm.android.presentation.screen.home_note_screen

import android.os.Bundle
import android.util.Log
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappwithkmm.android.presentation.theme.primaryColor
import com.example.noteappwithkmm.common.Constants.APP_TAG
import com.example.noteappwithkmm.common.utils.DataTimeUtil
import com.example.noteappwithkmm.common.utils.GenericMessageInfo
import com.example.noteappwithkmm.common.utils.GenericMessageInfoQueueUtil
import com.example.noteappwithkmm.domain.interactors.get_notes.SearchNoteOrAllNoteUseCase
import com.example.noteappwithkmm.domain.model.Note
import com.example.noteappwithkmm.presentation.home_notes_screen.HomeListEvent
import com.example.noteappwithkmm.presentation.home_notes_screen.HomeListNoteState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.example.noteappwithkmm.common.utils.Queue
import com.example.noteappwithkmm.domain.interactors.delte_note.DeleteNoteUseCase
import com.example.noteappwithkmm.domain.interactors.insert_update_note.InsertNoteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList


class HomeNotesViewModel constructor(
    private val savedStateHandle: Bundle,
    private val searchNoteOrAllNoteUseCase: SearchNoteOrAllNoteUseCase,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val genericMessageInfoQueueUtil: GenericMessageInfoQueueUtil,
    private val dataTimeUtil: DataTimeUtil
) : ViewModel() {
    var notesSate = mutableStateOf(HomeListNoteState())
        private set

    init {
        Log.d(APP_TAG, ": HomeNotesViewModel is inilization")

        //onTriggerEvent(HomeListEvent.LoadNotes)

    }

    fun onTriggerEvent(homeListEvent: HomeListEvent) {

        when (homeListEvent) {
            is HomeListEvent.LoadNotes -> {
                getNotes()
//                insertNote()
            }
            is HomeListEvent.NextPage -> {
                nextPage()
            }
            is HomeListEvent.OnQueryUpdate -> {

            }
            is HomeListEvent.ExecuteNewSearch -> {

            }
            is HomeListEvent.DeleteNote -> {
                  deleteNote(homeListEvent.note)
            }
            is HomeListEvent.OnRemoveHeaderMessageFromQueue -> {
                removeHeadMessage()
            }
            else -> {
                getNotes()
            }
        }
    }

    private fun deleteNote(note: Note) {
        deleteNoteUseCase.invoke(note)
            .onEach {
                notesSate.value = notesSate.value.copy(isLoading = it.isLoading)
                it.data?.let {
//                   onTriggerEvent(HomeListEvent.LoadNotes)
                    getNotes2()
                }
                it.message?.let {
                    appendToMessageQueue(it)
                }
            }.launchIn(viewModelScope)
    }

    private fun removeHeadMessage() {
        try {
            val queue = notesSate.value.queue
            queue.remove()
            notesSate.value = notesSate.value.copy(queue = Queue(mutableListOf()))
            notesSate.value = notesSate.value.copy(queue = queue)
        } catch (e: Exception) {
            //queue is empty
        }
    }
    private fun nextPage() {
        notesSate.value = notesSate.value.copy(page = notesSate.value.page + 1)
        getNotes()
    }

    private fun getNotes() {
        searchNoteOrAllNoteUseCase.invoke(notesSate.value.page, notesSate.value.query)
            .onEach {
                notesSate.value = notesSate.value.copy(isLoading = it.isLoading)
                it.data?.let {
                    Log.d(APP_TAG, "getNotes: ${it.toString()}")
                    appendNotes(it)
                }
                it.message?.let {
                    appendToMessageQueue(it)
                }
            }.launchIn(viewModelScope)
    }
    private fun getNotes2() {
        searchNoteOrAllNoteUseCase.invoke(notesSate.value.page, notesSate.value.query)
            .onEach {
                notesSate.value = notesSate.value.copy(isLoading = it.isLoading)
                it.data?.let {
                    Log.d(APP_TAG, "getNotes: ${it.toString()}")
                    notesSate.value = notesSate.value.copy(notes = it)
                }
                it.message?.let {
                    appendToMessageQueue(it)
                }
            }.launchIn(viewModelScope)
    }
    private fun insertNote(){
        val note=Note(
            null,
            "FirstTil",
            "FirstSub",
            "FirstBody",
            dataTimeUtil.now(),
            dataTimeUtil.now(),
            null,
            null,
            primaryColor.toArgb()
        )
        insertNoteUseCase.invoke(note)
            .onEach {
                it.data?.let {
                    Log.i(APP_TAG, "insertNote: $it")
                }
                it.message?.let {
                    appendToMessageQueue(it)
                }
            }.launchIn(viewModelScope)
    }


    private fun appendToMessageQueue(genericMessageInfo: GenericMessageInfo.Builder) {
        val queue = notesSate.value.queue

        if (!genericMessageInfoQueueUtil.doesMessageAlreadyExistInQueue(queue = queue,
                messageInfo = genericMessageInfo.build())
        ) {
            queue.add(genericMessageInfo.build())
            notesSate.value = notesSate.value.copy(queue = queue)
        }
    }

    private fun appendNotes(newNotes: List<Note>) {
        val curr = ArrayList(notesSate.value.notes)
        curr.addAll(newNotes)
        notesSate.value = notesSate.value.copy(notes = curr)

    }

    fun clearState() {
        notesSate.value=HomeListNoteState()
    }

}