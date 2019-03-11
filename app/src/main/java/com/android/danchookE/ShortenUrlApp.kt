package com.android.danchookE

import android.app.Application
import com.android.danchookE.di.appModules
import org.koin.android.ext.android.startKoin

class ShortenUrlApp : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules)
    }
}