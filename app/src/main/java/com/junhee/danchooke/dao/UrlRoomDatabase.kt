package com.junhee.danchooke.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.junhee.danchooke.model.ShortenUrl

@Database(entities = [ShortenUrl::class], version = 1)
abstract class UrlRoomDatabase : RoomDatabase() {

    abstract fun urlDao(): UrlDao

    companion object {
        @Volatile
        private var INSTANCE: UrlRoomDatabase? = null

        fun getDatabase(context: Context): UrlRoomDatabase {
            if (INSTANCE != null) {
                return INSTANCE as UrlRoomDatabase
            }
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    UrlRoomDatabase::class.java,
                    "url_database"
                ).build()
                return INSTANCE as UrlRoomDatabase
            }
        }
    }

}