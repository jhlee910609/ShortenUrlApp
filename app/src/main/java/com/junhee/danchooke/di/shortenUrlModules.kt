package com.junhee.danchooke.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.junhee.danchooke.model.NetworkRepositoryImpl
import com.junhee.danchooke.viewmodel.ShortenUrlViewModelFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val shortenUrlModules: Module = module {

    factory {
        NetworkRepositoryImpl(get())
    }

    factory {
        ShortenUrlViewModelFactory(get(), androidContext())
    }

    single {
        FirebaseAnalytics.getInstance(get())
    }
}