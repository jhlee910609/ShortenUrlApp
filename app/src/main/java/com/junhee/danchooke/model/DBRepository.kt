package com.junhee.danchooke.model

interface DBRepository {
    fun insert(shortenUrl: ShortenUrl)
}