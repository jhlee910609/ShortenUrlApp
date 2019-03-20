package com.junhee.danchooke.model

import io.reactivex.Single

interface NetworkRepository {
    fun getShortenUrl(url: String): Single<ShortenUrl>
}