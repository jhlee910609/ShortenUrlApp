package com.example.shortenurlapp.model

import io.reactivex.Single

interface Repository {
    fun getShortenUrl(url: String): Single<ShortenUrl>
}