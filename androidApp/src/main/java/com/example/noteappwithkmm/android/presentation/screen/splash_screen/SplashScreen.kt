package com.example.noteappwithkmm.android.presentation.screen.splash_screen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.noteappwithkmm.android.R
import com.example.noteappwithkmm.android.presentation.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDelayFinished: () -> Unit) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    AppTheme(displayProgressBar = true, onRemoveHeadFromQueue = {

    }) {
        // AnimationEffect
        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 0.7f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = {
                        OvershootInterpolator(4f).getInterpolation(it)
                    })
            )
            delay(3000L)
            onDelayFinished()
        }
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Image(painter = painterResource(id = R.drawable.app_icon), contentDescription = "")
            }
        }
    }
}