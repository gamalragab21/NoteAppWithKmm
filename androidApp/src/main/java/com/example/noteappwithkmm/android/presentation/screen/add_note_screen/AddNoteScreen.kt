@file:OptIn(ExperimentalMaterialApi::class)

package com.example.noteappwithkmm.android.presentation.screen.add_note_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import coil.compose.rememberImagePainter
import com.example.noteappwithkmm.android.common.Constants
import com.example.noteappwithkmm.android.common.Constants.timeMillis
import com.example.noteappwithkmm.android.presentation.components.shimmer.LoadingRecipeListShimmer
import com.example.noteappwithkmm.android.presentation.screen.add_note_screen.components.AddNoteTopBar
import com.example.noteappwithkmm.android.presentation.theme.*
import com.example.noteappwithkmm.common.Constants.APP_TAG
import com.example.noteappwithkmm.common.utils.DataTimeUtil
import com.example.noteappwithkmm.presentation.add_note_screen.AddNoteEvent
import com.example.noteappwithkmm.presentation.add_note_screen.AddNoteState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@Composable
fun AddNoteScreen(
    dataTimeUtil: DataTimeUtil,
    state: AddNoteState,
    title: String,
    subTitle: String,
    body: String,
    colorNote: Int,
    timeAdd: LocalDateTime,
    timeUpdate: LocalDateTime,
    eventFlow: SharedFlow<AddNoteViewModel.UIEvent>,
    onTriggerEvent: (AddNoteEvent) -> Unit,
    onBackClicked: () -> Unit,
) {


    AppTheme(displayProgressBar = state.isLoading,
        onRemoveHeadFromQueue = {
            onTriggerEvent(AddNoteEvent.OnRemoveHeaderMessageFromQueue)
        }, dialogQueue = state.queue) {
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            scaffoldState = scaffoldState,
            topBar = {

                AddNoteTopBar(onBackClicked = {
                    onBackClicked()
                }) {
                    onTriggerEvent(AddNoteEvent.SaveNote)
                }

            }
        ) {
            val timeValue = dataTimeUtil.humanizeDatetime(timeAdd)

            LaunchedEffect(key1 = true, block ={
                eventFlow.collectLatest {
                    when (it) {
                        is AddNoteViewModel.UIEvent.showSnakBar -> {
                            scaffoldState.snackbarHostState.showSnackbar(it.message)
                        }
                        is AddNoteViewModel.UIEvent.saveNote -> {
                            scaffoldState.snackbarHostState.showSnackbar("Saved Done")
                            onBackClicked()
                        }
                        is AddNoteViewModel.UIEvent.OnDelete -> {
                            scaffoldState.snackbarHostState.showSnackbar("Deleted Success")
                            onBackClicked()
                        }
                    }
                }
            } )
            LaunchedEffect(0) {
                while (true) {
                    state.note?.let {
                        if (it.noteId != null) {
                            onTriggerEvent(AddNoteEvent.OnQueryNoteUpdateTime)
                        } else {
                            onTriggerEvent(AddNoteEvent.OnQueryNoteUpdateTime)
                            onTriggerEvent(AddNoteEvent.OnQueryNoteAddTime)
                        }
                    }
                    delay(timeMillis) // Every minute
                }
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(it)) {
                TextFiledNoteTitle(timeValue = timeValue,
                    noteTitle = title,
                    onQueryChange = {
                        onTriggerEvent(AddNoteEvent.OnQueryNoteTitleUpdate(it))
                    })
                Log.i(APP_TAG, "AddNoteScreen:state ${state.note.toString()}")
                SubNoteTitleLayer(noteSubTitle = subTitle,
                    colorSelected = colorNote,
                    onQueryChange = {
                        onTriggerEvent(AddNoteEvent.OnQueryNoteSubTitleUpdate(it))
                    })


                NoteBodyLayer(noteBody = body, onQueryChange = {
                    onTriggerEvent(AddNoteEvent.OnQueryNoteBodyUpdate(it))
                })

                BottomSheetAddMiscellaneous(colorNote, state.note?.noteId, onDeleteClicked ={
                    onTriggerEvent(AddNoteEvent.DeleteNote(state.note!!))
                } ) {
//                    colorSelected.value=it.toArgb()
                    onTriggerEvent(AddNoteEvent.OnQueryNoteColorUpdate(it.toArgb()))
                }

            }

        }


    }

}

@Composable
fun BottomSheetAddMiscellaneous(
    colorSelected: Int,
    noteId: Int?,
    onDeleteClicked: () -> Unit,
    onColorSelected: (Color) -> Unit,
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState =
    BottomSheetState(BottomSheetValue.Collapsed))

    // Declaing Coroutine scope
    val coroutineScope = rememberCoroutineScope()

    // Creating a Bottom Sheet
    BottomSheetScaffold(
        modifier = Modifier.fillMaxWidth(),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            var circleSelected by remember { mutableStateOf(colorSelected) }
            var layout by remember { mutableStateOf<TextLayoutResult?>(null) }

            Box(
                Modifier
                    .fillMaxWidth()
                    .background(colorMiscellaneousBackground,
                        shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)),
            ) {


                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 10.dp),
                ) {
                    Text(text = "Add Miscellaneous",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.let {
                                        if (it.isCollapsed)
                                            it.expand()
                                        else it.collapse()
                                    }
                                }
                            },
                        style = MaterialTheme.typography.h1,
                        fontSize = 25.sp,
                        color = White,
                        fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier
                        .padding(vertical = 15.dp)
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        CircleSelectedColor(1, circleSelected, colorDefaultNoteColor, White) {
                            circleSelected = it
                            onColorSelected(colorDefaultNoteColor)
                        }
                        CircleSelectedColor(2, circleSelected, colorNote2, White) {
                            circleSelected = it
                            onColorSelected(colorNote2)

                        }
                        CircleSelectedColor(3, circleSelected, colorNote3, White) {
                            circleSelected = it
                            onColorSelected(colorNote3)

                        }
                        CircleSelectedColor(4, circleSelected, colorNote4, White) {
                            circleSelected = it
                            onColorSelected(colorNote4)

                        }
                        CircleSelectedColor(5, circleSelected, colorNote5, colorDefaultNoteColor) {
                            circleSelected = it
                            onColorSelected(colorNote5)

                        }

                        Text(text = "Pick Color",
                            modifier = Modifier
                                .drawBehind {
                                    layout?.let {
                                        val thickness = 5f
                                        val dashPath = 15f
                                        val spacingExtra = 4f
                                        val offsetY = 6f

                                        for (i in 0 until it.lineCount) {
                                            drawPath(
                                                path = Path().apply {
                                                    moveTo(it.getLineLeft(i),
                                                        it.getLineBottom(i) - spacingExtra + offsetY)
                                                    lineTo(it.getLineRight(i),
                                                        it.getLineBottom(i) - spacingExtra + offsetY)
                                                },
                                                Color.White,
                                                style = Stroke(width = thickness
                                                )
                                            )
                                        }
                                    }
                                }
                                .align(Alignment.CenterVertically),
                            onTextLayout = {
                                layout = it
                            },
                            style = MaterialTheme.typography.h5,
                            fontSize = 15.sp,
                            color = White,

                            fontWeight = FontWeight.Bold)


                    }

                    AddOptionText(painterResource(id = com.example.noteappwithkmm.android.R.drawable.ic_add_image),
                        text = "Add Image") {

                    }
                    AddOptionText(painterResource(id = com.example.noteappwithkmm.android.R.drawable.ic_web),
                        text = "Add Url") {

                    }
                    AddOptionText(painterResource(id = com.example.noteappwithkmm.android.R.drawable.ic_copy),
                        text = "Copy") {

                    }
                    noteId?.let {
                        AddOptionText(
                            painterResource(id = com.example.noteappwithkmm.android.R.drawable.ic_delete),
                            tint = RedErrorDark,
                            "Delete This Note", textColor = RedErrorDark,
                        ) {
                            onDeleteClicked()
                        }
                    }

                }
            }
        },
        sheetPeekHeight = 50.dp
    ) {}

}

@Composable
fun AddOptionText(
    painter: Painter,
    tint: Color = colorIcon,
    text: String,
    textColor: Color = White,
    onItemClicked: () -> Unit,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemClicked() }
        .padding(vertical = 8.dp)) {
        Icon(painter = painter,
            modifier = Modifier
                .padding(end = 10.dp)
                .width(20.dp)
                .height(20.dp),
            tint = tint,
            contentDescription = "")

        Text(text = text,
            modifier = Modifier,
            style = MaterialTheme.typography.h6,
            fontSize = 15.sp,
            color = textColor,
            fontWeight = FontWeight.ExtraBold)

    }
}

@Composable
fun CircleSelectedColor(
    id: Int,
    circleSelected: Int,
    color: Color,
    colorIconDone: Color,
    onItemClicked: (Int) -> Unit,
) {
    Box(modifier = Modifier
        .wrapContentSize()
        .clip(CircleShape)
        .border(3.dp, color, CircleShape)
        .padding(6.dp)
        .background(Transparent)
        .clickable {
            if (circleSelected != color.toArgb())
                onItemClicked(color.toArgb())
        }, contentAlignment = Alignment.Center) {
        Box(modifier = Modifier
            .wrapContentSize()
            .background(color, CircleShape)
            .padding(5.dp)
        ) {
            Icon(painter = rememberImagePainter(com.example.noteappwithkmm.android.R.drawable.ic_done),
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp),
                tint = if (circleSelected == color.toArgb()) colorIconDone else Transparent,
                contentDescription = "")
        }
    }


}

@Composable
fun NoteBodyLayer(onQueryChange: (String) -> Unit, noteBody: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 15.dp, horizontal = 10.dp)) {
        var value by remember { mutableStateOf(TextFieldValue(noteBody)) }

        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = { newText ->
                value = newText
                onQueryChange(value.text)
            },
            placeholder = {
                Text(text = "Type Note Here...",
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.SemiBold,
                    color = colorIcon)
            },
            textStyle = MaterialTheme.typography.h5,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                textColor = Color.White
            )

        )
    }
}

@Composable
fun SubNoteTitleLayer(onQueryChange: (String) -> Unit, noteSubTitle: String, colorSelected: Int) {
    var value by remember { mutableStateOf(TextFieldValue(noteSubTitle)) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 15.dp, horizontal = 10.dp),
        horizontalArrangement = Arrangement.Start) {
        Spacer(modifier = Modifier
            .width(5.dp)
            .height(50.dp)
            .align(Alignment.CenterVertically)
            .background(Color(colorSelected), MaterialTheme.shapes.small))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
            value = value,
            onValueChange = { newText ->
                value = newText
                onQueryChange(value.text)
            },
            placeholder = {
                Text(text = "Note SubTitle",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.h3,
                    fontWeight = FontWeight.Bold,
                    color = colorIcon)
            },
            textStyle = MaterialTheme.typography.h4,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                textColor = Color.White
            )

        )
    }


}

@Composable
fun TextFiledNoteTitle(
    timeValue: String,
    noteTitle: String,
    onQueryChange: (value: String) -> Unit,
) {
    var value by remember { mutableStateOf(TextFieldValue(noteTitle)) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp, horizontal = 10.dp)) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = { newText ->
                value = newText
                onQueryChange(value.text)
            },
            placeholder = {
                Text(text = "Note Title",
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.h2,
                    fontWeight = FontWeight.Bold,
                    color = colorIcon)
            },
            textStyle = MaterialTheme.typography.h2,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                textColor = Color.White
            )

        )
        Text(text = timeValue,
            fontSize = 15.sp,
            style = MaterialTheme.typography.h3,
            color = colorIcon,
            modifier = Modifier.fillMaxWidth())
    }
}
