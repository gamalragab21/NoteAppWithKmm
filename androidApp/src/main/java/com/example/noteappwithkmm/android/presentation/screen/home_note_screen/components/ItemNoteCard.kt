package com.example.noteappwithkmm.android.presentation.screen.home_note_screen.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import coil.compose.rememberImagePainter
import com.example.noteappwithkmm.android.presentation.theme.*
import com.example.noteappwithkmm.common.Constants.APP_TAG
import com.example.noteappwithkmm.common.utils.DataTimeUtil
import com.example.noteappwithkmm.domain.model.Note
import io.github.aakira.napier.Napier

@Composable
fun ItemNoteCard(
    note: Note,
    dataTimeUtil: DataTimeUtil,
    onDeleteNote: (Note) -> Unit,

    onSelectedNote: (Int) -> Unit,
) {

    val noteColor = Color(note.noteColor) // the note back ground
    Napier.d(tag = APP_TAG, message = note.toString())
    val textNoteColor =
        if ((noteColor == colorDefaultNoteColor) or (noteColor==primaryColor)) White else if (noteColor == colorNote5) Black2  else colorDefaultNoteColor

    val textUrlColor = if (noteColor == primaryColor) White else primaryColor
    Card(modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(0.45f)
        .wrapContentHeight()
        .background(color = noteColor, shape = MaterialTheme.shapes.large)
        .clickable { onSelectedNote(note.noteId!!) },
        elevation = 5.dp,
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(noteColor)
            .padding(5.dp)) {
            Text(text = note.noteTitle,
                style = MaterialTheme.typography.h2, fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textNoteColor)
            Spacer(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent, shape = MaterialTheme.shapes.small)
                .padding(vertical = 3.dp))
            Text(text = note.noteSubTitle,
                style = MaterialTheme.typography.h3,
                fontSize = 13.sp,
                color = textNoteColor)
            Spacer(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent, shape = MaterialTheme.shapes.small)
                .padding(vertical = 3.dp))
            note.noteUrl?.let { noteUrl ->
                Text(text = noteUrl, style = MaterialTheme.typography.h3, color = textUrlColor)
                Spacer(modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent, shape = MaterialTheme.shapes.small)
                    .padding(vertical = 2.dp))
            }
            Text(text = dataTimeUtil.humanizeDatetime(note.noteTimeUpdated),
                style = MaterialTheme.typography.h4,
                fontSize = 11.sp,
                color = textNoteColor)
            Spacer(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent, shape = MaterialTheme.shapes.small)
                .padding(vertical = 2.dp))
            Icon(rememberImagePainter(com.example.noteappwithkmm.android.R.drawable.ic_delete),
                contentDescription = "",
                tint = Red,
                modifier = Modifier
                    .clickable { onDeleteNote(note) }
                    .padding(5.dp)
                    .align(Alignment.End))
            Spacer(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent, shape = MaterialTheme.shapes.small)
                .padding(vertical = 2.dp))
        }


    }


}