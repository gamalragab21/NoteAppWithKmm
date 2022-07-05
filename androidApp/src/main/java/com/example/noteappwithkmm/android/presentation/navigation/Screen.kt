package com.example.noteappwithkmm.android.presentation.navigation

sealed class Screen(val route: String) {

    object SplashScreen : Screen("splash_screen")
    object HomeNotesScreen : Screen("home_notes_screen")
    object AddNoteScreen : Screen("add_note_screen")

    fun withArgs(vararg args: String): String = buildString {
        append(route)
        args.forEach { arg ->
            append("/$arg")
        }
    }
}

