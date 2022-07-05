@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.noteappwithkmm.android.presentation.screen.home_note_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noteappwithkmm.android.presentation.theme.Black1
import com.example.noteappwithkmm.android.presentation.theme.colorDefaultNoteColor
import com.example.noteappwithkmm.android.presentation.theme.primaryTextColor

@Composable
fun SearchAppBar(
    onQueryChange: (String) -> Unit,
    onExecuteSearch: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var value by remember { mutableStateOf(TextFieldValue("")) }


    Surface(
        modifier = Modifier.fillMaxWidth(),
        elevation = 8.dp,
    ) {
        Column {
            Text(modifier = Modifier
                .padding(top = 10.dp, start = 10.dp, bottom = 10.dp)
                .fillMaxWidth(),
                text = "My Note",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h1,
                color = Color.White)
            Row(
                modifier = Modifier
                    .padding(top = 15.dp)
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(colorDefaultNoteColor, MaterialTheme.shapes.large),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = White,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 5.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    value = value,
                    onValueChange = { newText ->
                        value = newText
                        onQueryChange(value.text)
                    },
                    label = {
                        Text(text = "Search notes",
                            fontSize = 15.sp,
                            color = White)
                    },

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onExecuteSearch(value.text)
                            keyboardController?.hide()
                        },
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Transparent,
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent,
                        textColor=White
                        //Color of indicator = underbar
                    )

                )
            }
        }
    }
}


