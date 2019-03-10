package com.example.shortenurlapp

import android.app.Application
import com.example.shortenurlapp.di.appModules
import org.koin.android.ext.android.startKoin

class ShortenUrlApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules)
    }
}