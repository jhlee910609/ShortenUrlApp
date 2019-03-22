package com.junhee.danchooke.model

import androidx.lifecycle.LiveData

interface DBRepository {
    fun insert(shortenUrl: ShortenUrl)
    fun getAllUrls():LiveData<List<ShortenUrl>>
}