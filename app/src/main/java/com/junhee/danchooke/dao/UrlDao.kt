package com.junhee.danchooke.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.junhee.danchooke.model.ShortenUrl

@Dao
interface UrlDao {
    @Query("SELECT * from url_table ORDER by orgUrl ASC")
    fun getAllUrls(): LiveData<List<ShortenUrl>>

    @Insert
    fun insertUrl(shortenUrl: ShortenUrl)
}