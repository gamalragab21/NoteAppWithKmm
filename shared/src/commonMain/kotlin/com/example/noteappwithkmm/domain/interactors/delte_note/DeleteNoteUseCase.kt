package com.example.noteappwithkmm.domain.interactors.delte_note

import com.example.noteappwithkmm.common.Constants.APP_TAG
import com.example.noteappwithkmm.common.DataState
import com.example.noteappwithkmm.common.utils.GenericMessageInfo
import com.example.noteappwithkmm.common.utils.UIComponentType
import com.example.noteappwithkmm.dataSource.cache.repositories.NoteCacheService
import com.example.noteappwithkmm.domain.model.Note
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteNoteUseCase  constructor(
    private val noteCacheService: NoteCacheService,
) {

    fun invoke(
       note: Note
    ): Flow<DataState<Boolean>> = flow {
        // how we can handle loading?
        emit(DataState.loading(true))

        try {
                noteCacheService.deleteNote(note)
            emit(DataState.data(data = true))
            Napier.d(tag = APP_TAG, message = "Insert success")
        } catch (e: Exception) {
            emit(DataState.error(
                GenericMessageInfo.Builder().id("Insert Note Error")
                    .title("Error")
                    .uiComponentType(UIComponentType.Dialog)
                    .description(e.message?:"UnKnown Error")
            ))
            // how we can handle an error?
        }

    }


}