package com.example.noteappwithkmm.android.di

import android.os.Bundle
import com.example.noteappwithkmm.android.presentation.screen.add_note_screen.AddNoteViewModel
import com.example.noteappwithkmm.android.presentation.screen.home_note_screen.HomeNotesViewModel
import com.example.noteappwithkmm.dataSource.cache.DriverFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule= module{
    single<DriverFactory> {  DriverFactory(get()) }
}
val viewModelModule= module {
    viewModel { (savedStateHandle: Bundle)  ->
        HomeNotesViewModel(savedStateHandle,get(),get(),get(),get(),get())
    }
    viewModel { (savedStateHandle: Bundle)  ->
        AddNoteViewModel(savedStateHandle,get(),get(),get(),get(),get())
    }
}
