package com.example.noteappwithkmm.domain.interactors.get_note_details

import com.example.noteappwithkmm.common.DataState
import com.example.noteappwithkmm.common.utils.GenericMessageInfo
import com.example.noteappwithkmm.common.utils.UIComponentType
import com.example.noteappwithkmm.dataSource.cache.repositories.NoteCacheService
import com.example.noteappwithkmm.domain.model.Note
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNoteByIdUseCase  constructor(
    private val noteCacheService: NoteCacheService,
) {

    fun invoke(
      noteId:Int
    ): Flow<DataState<Note>> = flow {
        // how we can handle loading?
        emit(DataState.loading(true))

        try {
           val cacheResult= noteCacheService.getNoteById(noteId)
            emit(DataState.data(data = cacheResult))
        } catch (e: Exception) {
            emit(DataState.error(
                GenericMessageInfo.Builder().id("Notes Error")
                    .title("Error")
                    .uiComponentType(UIComponentType.Dialog)
                    .description(e.message?:"UnKnown Error")
            ))
            // how we can handle an error?
        }

    }


}