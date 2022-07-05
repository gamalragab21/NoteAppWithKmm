package com.example.noteappwithkmm.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.noteappwithkmm.android.presentation.screen.add_note_screen.AddNoteScreen
import com.example.noteappwithkmm.android.presentation.screen.add_note_screen.AddNoteViewModel
import com.example.noteappwithkmm.android.presentation.screen.home_note_screen.HomeNotesScreen
import com.example.noteappwithkmm.android.presentation.screen.home_note_screen.HomeNotesViewModel
import com.example.noteappwithkmm.android.presentation.screen.splash_screen.SplashScreen
import com.example.noteappwithkmm.common.utils.DataTimeUtil
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = Screen.SplashScreen.route) {

        //TODO: splashScreen
        composable(route = Screen.SplashScreen.route) { navBackStackEntry ->
            SplashScreen {
                navController.navigate(Screen.HomeNotesScreen.route) {
                    popUpTo(Screen.SplashScreen.route) {
                        inclusive = true
                    }
                }
            }
        }

        // TODO: mainScreen
        composable(route = Screen.HomeNotesScreen.route) { navBackStackEntry ->
            val viewModel: HomeNotesViewModel =
                getViewModel { parametersOf(navBackStackEntry.arguments) }
            val dataTimeUtil = get<DataTimeUtil>()

            HomeNotesScreen(viewModel.notesSate.value, dataTimeUtil, viewModel::onTriggerEvent,
                onAddImageClicked = {
                    navController.navigate(Screen.AddNoteScreen.withArgs("-1", it, "-1"))
                    viewModel.clearState()

                }, onAddNoteClicked = {
                    navController.navigate(Screen.AddNoteScreen.withArgs("-1", "-1", "-1"))
                    viewModel.clearState()

                }, onAddWebLinkClicked = {
                    navController.navigate(Screen.AddNoteScreen.withArgs("-1", "-1", it))
                    viewModel.clearState()
                }) { noteId ->
                navController.navigate(Screen.AddNoteScreen.withArgs(noteId.toString(), "-1", "-1"))
                viewModel.clearState()

            }
        }

        // TODO: SecondScreen
        composable(
            route = Screen.AddNoteScreen.route + "/{noteId}/{noteImage}/{noteWeblink}",
            arguments = listOf(
                navArgument("noteId") {
                    this.nullable = true
                    this.type = NavType.StringType
                },
                navArgument("noteImage") {
                    this.nullable = true
                    this.type = NavType.StringType
                },
                navArgument("noteWeblink") {
                    this.nullable = true
                    this.type = NavType.StringType
                },
            )
        ) { navBackStackEntry ->
            val viewModel: AddNoteViewModel =
                getViewModel { parametersOf(navBackStackEntry.arguments) }
            AddNoteScreen(dataTimeUtil = viewModel.dataTimeUtil,
                viewModel.noteState.value,
                viewModel.noteTitleSate.value,
                viewModel.noteSubTitleSate.value,
                viewModel.noteBodySate.value,
                viewModel.noteColorSate.value,
                viewModel.noteDateAddSate.value,
                viewModel.noteDateUpdateSate.value,
                viewModel.eventFlow,
                onTriggerEvent = viewModel::onTriggerEvent,
                onBackClicked = {
                    navController.popBackStack()
                })
        }
    }
}