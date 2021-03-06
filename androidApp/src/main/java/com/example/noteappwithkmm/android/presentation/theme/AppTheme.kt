package com.example.noteappwithkmm.android.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.noteappwithkmm.android.presentation.components.CircularIndeterminateProgressBar
import com.example.noteappwithkmm.android.presentation.components.ProcessDialogQueue
import com.example.noteappwithkmm.android.presentation.theme.*
import com.example.noteappwithkmm.common.utils.GenericMessageInfo
import com.example.noteappwithkmm.common.utils.Queue

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    displayProgressBar: Boolean,
    dialogQueue: Queue<GenericMessageInfo> = Queue(mutableListOf()),
    onRemoveHeadFromQueue: () -> Unit,
    content: @Composable () -> Unit,
) {
// TODO: Add Colors
    val colors =
        //if (darkTheme) {
//        darkColors()
//            .copy(
//                primary = primaryColor,
//                primaryVariant = primaryVariantColor,
//                secondary = secondaryDarkColor,
//                secondaryVariant = secondaryLightColor,
//                onPrimary = Color.White,
//                onError = RedErrorDark,
//                background = primaryDarkColor,
//                surface = Color.White
//            )
//    } else {
        lightColors()
            .copy(
                primary = primaryColor,
                primaryVariant = primaryVariantColor,
                secondary = secondaryColor,
                secondaryVariant = secondaryLightColor,
                onPrimary = Color.Black,
                onError = RedErrorDark,
                background = primaryLightColor,
                surface = primaryLightColor
            )
   // }
// TODO: Add Theme
    MaterialTheme(
        colors = colors,
        typography = QuickSandTypography,
        shapes = AppShapes
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Grey1)
        ) {
            ProcessDialogQueue(dialogQueue = dialogQueue) {
                onRemoveHeadFromQueue()
            }
            content()
//            LoadingRecipeListShimmer(
//                displayProgressBar,
//                Constants.RECIPE_IMAGE_HEIGHT.dp
//            )
            CircularIndeterminateProgressBar(isDisplayed = displayProgressBar, verticalBias = 0.3f)
        }
    }
}