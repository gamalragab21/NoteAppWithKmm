package com.example.noteappwithkmm.android.presentation.screen.add_note_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.noteappwithkmm.android.presentation.theme.colorIcon

@Composable
fun AddNoteTopBar(
    onBackClicked:()->Unit,
    onSaveClicked:()->Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 15.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Icon(rememberImagePainter(com.example.noteappwithkmm.android.R.drawable.ic_back),
            contentDescription = "back Icon",
            tint = colorIcon,
            modifier = Modifier
                .width(35.dp).height(35.dp).padding(3.dp)
                .clickable { onBackClicked() }
        )
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .wrapContentSize()
            .padding(3.dp)
            .background(Color.Transparent, shape = CircleShape)
            .border(1.dp, color = colorIcon, shape = CircleShape)
            .clickable {
                onSaveClicked()
            }
        ) {
            Icon(rememberImagePainter(com.example.noteappwithkmm.android.R.drawable.ic_done),
                contentDescription = "done Icon",
                tint = colorIcon,
                modifier = Modifier
                    .width(30.dp).height(30.dp)
            )
        }
    }
}