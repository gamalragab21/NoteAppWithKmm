package com.example.noteappwithkmm.android

import android.app.Application
import com.example.noteappwithkmm.android.di.appModule
import com.example.noteappwithkmm.android.di.viewModelModule
import com.example.noteappwithkmm.di.commonAppModule
import com.example.noteappwithkmm.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyBaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyBaseApplication)
            modules(commonAppModule,appModule,viewModelModule)
            androidLogger()
        }
    }

}