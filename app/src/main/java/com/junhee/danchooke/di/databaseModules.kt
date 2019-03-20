package com.junhee.danchooke.di

import com.junhee.danchooke.dao.UrlRoomDatabase
import com.junhee.danchooke.model.DBRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val databaseModules: Module = module {

    single {
        UrlRoomDatabase.getDatabase(androidContext()).urlDao()
    }

    factory {
        DBRepositoryImpl(get())
    }
}