package com.junhee.danchooke.api

import com.junhee.danchooke.model.ShortenUrl

data class ShortenUrlResponse(
    val msg: String,
    val result: ShortenUrl,
    val code: String
)
