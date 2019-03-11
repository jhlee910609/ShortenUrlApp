package com.android.danchookE.api

import com.android.danchookE.model.ShortenUrl

data class ShortenUrlResponse(
    val msg: String,
    val result: ShortenUrl,
    val code: String
)
