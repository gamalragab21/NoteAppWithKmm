package com.example.noteappwithkmm.android.presentation.screen.add_note_screen

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappwithkmm.android.presentation.theme.colorDefaultNoteColor
import com.example.noteappwithkmm.common.Constants.APP_TAG
import com.example.noteappwithkmm.common.utils.*
import com.example.noteappwithkmm.common.utils.Queue
import com.example.noteappwithkmm.domain.interactors.delte_note.DeleteNoteUseCase
import com.example.noteappwithkmm.domain.interactors.get_note_details.GetNoteByIdUseCase
import com.example.noteappwithkmm.domain.interactors.insert_update_note.InsertNoteUseCase
import com.example.noteappwithkmm.domain.model.Note
import com.example.noteappwithkmm.presentation.add_note_screen.AddNoteEvent
import com.example.noteappwithkmm.presentation.add_note_screen.AddNoteState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*

class AddNoteViewModel constructor(
    private val savedStateHandle: Bundle,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val genericMessageInfoQueueUtil: GenericMessageInfoQueueUtil,
    val dataTimeUtil: DataTimeUtil,
) : ViewModel() {

    private val _noteState = mutableStateOf(AddNoteState())
    val noteState: State<AddNoteState> = _noteState

    var noteTitleSate = mutableStateOf("")
        private set
    var noteSubTitleSate = mutableStateOf("")
        private set
    var noteBodySate = mutableStateOf("")
        private set
    var noteColorSate = mutableStateOf(colorDefaultNoteColor.toArgb())
        private set
    var noteDateAddSate = mutableStateOf(dataTimeUtil.now())
        private set
    var noteDateUpdateSate = mutableStateOf(dataTimeUtil.now())
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
//        val time = "${dataTimeUtil.now()}"
//        Log.i(APP_TAG, "time is : $time")
//        val initNote = Note(null,
//            "",
//            "",
//            "",
//            dataTimeUtil.now(),
//            dataTimeUtil.now(),
//            null,
//            null,
//            colorDefaultNoteColor.toArgb())
//        noteSate.value = noteSate.value.copy(note = initNote)
        savedStateHandle.getString("noteId")?.let {
            if (it != "-1") {
                onTriggerEvent(AddNoteEvent.LoadNote(it))
                currentNoteId = it.toInt()
            }
        }
        savedStateHandle.getString("noteImage")?.let {
            // onTriggerEvent(AddNoteEvent.LoadNote(it))
        }
        savedStateHandle.getString("noteWeblink")?.let {
            //  onTriggerEvent(AddNoteEvent.LoadNote(it))
        }
    }

    fun onTriggerEvent(event: AddNoteEvent) {
        when (event) {
            is AddNoteEvent.LoadNote -> {
                getNoteById(noteId = event.noteId.toInt())
            }
            is AddNoteEvent.OnQueryNoteAddTime -> {
                noteDateAddSate.value = dataTimeUtil.now()
            }
            is AddNoteEvent.OnQueryNoteUpdateTime -> {
                noteDateUpdateSate.value = dataTimeUtil.now()
            }
            is AddNoteEvent.OnQueryNoteTitleUpdate -> {
                noteTitleSate.value = event.query
            }
            is AddNoteEvent.OnQueryNoteSubTitleUpdate -> {
                noteSubTitleSate.value = event.query
            }
            is AddNoteEvent.OnQueryNoteBodyUpdate -> {
                noteBodySate.value = event.query
            }
            is AddNoteEvent.OnQueryNoteColorUpdate -> {
                noteColorSate.value = event.color
            }
            is AddNoteEvent.DeleteNote -> {
                deleteNote(event.note)
            }
            is AddNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    _noteState.value =
                        noteState.value.copy(note = Note(noteTitle = noteTitleSate.value,
                            noteColor = noteColorSate.value,
                            noteBody = noteBodySate.value,
                            noteImage = null,
                            noteUrl = null,
                            noteTimeUpdated = noteDateUpdateSate.value,
                            noteTimeAdded = noteDateAddSate.value,
                            noteSubTitle = noteSubTitleSate.value, noteId = currentNoteId))
                    _noteState.value.note?.let {
                        if (it.noteTitle.isEmpty()) {
                            _eventFlow.emit(UIEvent.showSnakBar("Note Title is required"))
                            showErrorInputs(description="Note Title is required")
                        } else if (it.noteSubTitle.isEmpty()) {
                            _eventFlow.emit(UIEvent.showSnakBar("Note Subtitle is required"))
                            showErrorInputs(description="Note Subtitle is required")
                        } else if (it.noteBody.isEmpty()) {
                            _eventFlow.emit(UIEvent.showSnakBar("Note Body is required"))
                            showErrorInputs(description="Note Body is required")
                        } else saveNote()
                    }
                }
            }
            is AddNoteEvent.OnRemoveHeaderMessageFromQueue -> {
                removeHeadMessage()
            }
        }
    }

    private fun deleteNote(note: Note) {
        deleteNoteUseCase.invoke(note)
            .onEach {
                _noteState.value = _noteState.value.copy(isLoading = it.isLoading)
                it.data?.let {
                  _eventFlow.emit(UIEvent.OnDelete)
                }
                it.message?.let {
                    appendToMessageQueue(it)
                }
            }.launchIn(viewModelScope)
    }

    private fun showErrorInputs(
        title: String = "Invalid!",
        description: String = "This Filed Is required",
    ) {
        val messageInfoBuilder = GenericMessageInfo.Builder()
            .id(description)
            .title(title)
            .uiComponentType(UIComponentType.Dialog)
            .description(description)
            .positive(PositiveAction(
                positiveBtnTxt = "Ok",
                onPositiveAction = {
                     removeHeadMessage()
                }
            ))
            .negative(NegativeAction(
                negativeBtnTxt = "Cancel",
                onNegativeAction = {
                    removeHeadMessage()
                }
            ))
        appendToMessageQueue(messageInfoBuilder)
    }

    private fun saveNote() {
        insertNoteUseCase.invoke(noteState.value.note!!)
            .onEach {
                _noteState.value = noteState.value.copy(isLoading = it.isLoading)
                it.data?.let {
                    _eventFlow.emit(UIEvent.saveNote)
                }
                it.message?.let {
                    appendToMessageQueue(it)
                }
            }.launchIn(viewModelScope)
    }

    private fun getNoteById(noteId: Int) {
        getNoteByIdUseCase.invoke(noteId)
            .onEach {
                _noteState.value = noteState.value.copy(isLoading = it.isLoading)
                it.data?.let {
                    appendNote(it)
                }
                it.message?.let {
                    appendToMessageQueue(it)
                }
            }.launchIn(viewModelScope)
    }

    private fun appendNote(note: Note) {
        _noteState.value = noteState.value.copy(note = note)
        noteTitleSate.value = noteState.value.note?.noteTitle ?: ""
        noteSubTitleSate.value = noteState.value.note?.noteSubTitle ?: ""
        noteBodySate.value = noteState.value.note?.noteBody ?: ""
        noteColorSate.value = noteState.value.note?.noteColor ?: colorDefaultNoteColor.toArgb()
        noteDateAddSate.value = noteState.value.note?.noteTimeAdded ?: dataTimeUtil.now()
        noteDateUpdateSate.value = noteState.value.note?.noteTimeUpdated ?: dataTimeUtil.now()
    }

    private fun removeHeadMessage() {
        try {
            val queue = _noteState.value.queue
            queue.remove()
            _noteState.value = noteState.value.copy(queue = Queue(mutableListOf()))
            _noteState.value = noteState.value.copy(queue = queue)
        } catch (e: Exception) {
            //queue is empty
        }
    }

    private fun appendToMessageQueue(genericMessageInfo: GenericMessageInfo.Builder) {
        val queue = _noteState.value.queue

        if (!genericMessageInfoQueueUtil.doesMessageAlreadyExistInQueue(queue = queue,
                messageInfo = genericMessageInfo.build())
        ) {
            queue.add(genericMessageInfo.build())
            _noteState.value = noteState.value.copy(queue = queue)
        }
    }

    sealed class UIEvent() {
        data class showSnakBar(val message: String) : UIEvent()
        object saveNote : UIEvent()
        object OnDelete : UIEvent()
    }

}