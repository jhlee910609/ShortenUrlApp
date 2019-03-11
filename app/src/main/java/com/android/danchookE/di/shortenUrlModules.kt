package com.android.danchookE.di

import com.android.danchookE.model.NetworkRepositoryImpl
import com.android.danchookE.model.Repository
import com.android.danchookE.viewmodel.ShortenUrlViewModelFactory
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val shortenUrlModules: Module = module {
    factory {
        NetworkRepositoryImpl(get()) as Repository
    }

    factory {
        ShortenUrlViewModelFactory(get())
    }
}