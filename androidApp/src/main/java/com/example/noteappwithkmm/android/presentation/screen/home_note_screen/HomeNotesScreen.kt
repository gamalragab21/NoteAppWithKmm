package com.example.noteappwithkmm.android.presentation.screen.home_note_screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.example.noteappwithkmm.android.common.Constants
import com.example.noteappwithkmm.android.presentation.components.EmptyScreen
import com.example.noteappwithkmm.android.presentation.components.shimmer.LoadingRecipeListShimmer
import com.example.noteappwithkmm.android.presentation.screen.home_note_screen.components.ItemNoteCard
import com.example.noteappwithkmm.android.presentation.screen.home_note_screen.components.SearchAppBar
import com.example.noteappwithkmm.android.presentation.theme.*
import com.example.noteappwithkmm.common.Constants.APP_TAG
import com.example.noteappwithkmm.common.Constants.NOTE_PAGINATION_PAGE_SIZE
import com.example.noteappwithkmm.common.utils.DataTimeUtil
import com.example.noteappwithkmm.domain.model.Note
import com.example.noteappwithkmm.presentation.home_notes_screen.HomeListEvent
import com.example.noteappwithkmm.presentation.home_notes_screen.HomeListNoteState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeNotesScreen(
    state: HomeListNoteState,
    dataTimeUtil: DataTimeUtil,
    onTriggerEvent: (HomeListEvent) -> Unit,
    onAddNoteClicked: () -> Unit,
    onAddImageClicked: (String) -> Unit,
    onAddWebLinkClicked: (String) -> Unit,
    onSelectedNote: (Int) -> Unit,
) {
    Log.i(APP_TAG, "HomeNotesScreen: ${state.toString()}")

    LaunchedEffect(key1 = true){
        onTriggerEvent(HomeListEvent.LoadNotes)
    }
    AppTheme(displayProgressBar = state.isLoading,
        dialogQueue = state.queue,
        onRemoveHeadFromQueue = {
            onTriggerEvent(HomeListEvent.OnRemoveHeaderMessageFromQueue)
        }) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                SearchAppBar(onExecuteSearch = {
                    onSelectedNote(10)
                }, onQueryChange = {

                })
            }, floatingActionButton = {
                FloatingActionButton(onClick = {
                    onAddNoteClicked()
                },
                    shape = CircleShape,
                    backgroundColor = primaryColor,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(bottom = 10.dp)
                ) {

                    Icon(imageVector = Icons.Default.Add,
                        tint = Color.White,
                        contentDescription = "",
                        modifier = Modifier)
                }
            }
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                if (state.isLoading && state.notes.isEmpty()) {
                    // loading
                    LoadingRecipeListShimmer(
                        Constants.ITEM_NOTE_IMAGE_HEIGHT.dp
                    )
                } else if (state.notes.isEmpty()) {
                    Box(modifier = Modifier
                        .padding(top = 15.dp, start = 5.dp, end = 5.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f), contentAlignment = Alignment.Center) {
                        EmptyScreen()
                    }
                } else {
                    LazyVerticalGrid(modifier = Modifier
                        .padding(top = 15.dp, start = 5.dp, end = 5.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        cells = GridCells.Fixed(2)) {
                        itemsIndexed(state.notes) { index, note ->
                            if ((index + 1) >= (state.page * NOTE_PAGINATION_PAGE_SIZE) && !state.isLoading) {
                                onTriggerEvent(HomeListEvent.NextPage)
                            }
                            ItemNoteCard(note, dataTimeUtil, onDeleteNote = {
                                onTriggerEvent(HomeListEvent.DeleteNote(it))
                            }) {
                                onSelectedNote(it)
                            }
                        }
                    }
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .background(colorQuickActionBackground)
                    .fillMaxHeight(1f)
                    .padding(vertical = 15.dp),
                    verticalArrangement = Arrangement.Bottom) {
                    BottomHomeIcons(onAddNoteClicked, onAddImageClicked, onAddWebLinkClicked)
                }

            }
        }
    }
}

@Composable
fun BottomHomeIcons(
    onAddNoteClicked: () -> Unit,
    onAddImageClicked: (String) -> Unit,
    onAddWebLinkClicked: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(colorQuickActionBackground),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(painter = rememberImagePainter(com.example.noteappwithkmm.android.R.drawable.ic_add_circle_outline),
            contentDescription = "Search Icon",
            tint = colorIcon,
            modifier = Modifier
                .width(45.dp)
                .height(35.dp)
                .padding(horizontal = 5.dp)
                .clickable {
                    onAddNoteClicked()
                })

        Icon(painter = rememberImagePainter(com.example.noteappwithkmm.android.R.drawable.ic_home_add_image),
            contentDescription = "Search Icon",
            tint = colorIcon,
            modifier = Modifier
                .width(45.dp)
                .height(35.dp)
                .padding(horizontal = 5.dp)
                .clickable {
                    onAddImageClicked("image")
                })

        Icon(painter = rememberImagePainter(com.example.noteappwithkmm.android.R.drawable.ic_web),
            contentDescription = "Search Icon",
            tint = colorIcon,
            modifier = Modifier
                .width(45.dp)
                .height(35.dp)
                .padding(horizontal = 5.dp)
                .clickable {
                    onAddWebLinkClicked("weblink")
                })

    }
}
