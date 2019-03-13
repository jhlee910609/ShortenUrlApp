package com.junhee.danchooke

import android.app.Application
import com.junhee.danchooke.di.appModules
import org.koin.android.ext.android.startKoin

class ShortenUrlApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules)

    }
}