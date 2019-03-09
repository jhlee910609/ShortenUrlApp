package com.example.shortenurlapp.api

import com.example.shortenurlapp.model.ShortenUrl

data class ShortenUrlResponse(
    val msg: String,
    val result: ShortenUrl,
    val code: String
)
