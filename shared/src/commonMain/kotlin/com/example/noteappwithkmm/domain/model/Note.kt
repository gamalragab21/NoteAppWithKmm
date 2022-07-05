package com.example.noteappwithkmm.domain.model


import kotlinx.datetime.LocalDateTime

data class Note(
    val noteId:Int?,
    var noteTitle: String,
    var noteSubTitle: String,
    var noteBody: String,
    var noteTimeAdded: LocalDateTime,
    var noteTimeUpdated: LocalDateTime,
    val noteImage: String?,
    val noteUrl: String?,
    var noteColor: Int,
)