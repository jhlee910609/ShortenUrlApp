package com.android.danchookE.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("v1/util/shorturl")
    fun shortenUrl(@Query("url") url: String): Single<ShortenUrlResponse>
}
