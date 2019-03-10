package com.example.shortenurlapp.di

import com.example.shortenurlapp.model.NetworkRepositoryImpl
import com.example.shortenurlapp.model.Repository
import com.example.shortenurlapp.viewmodel.ShortenUrlViewModelFactory
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