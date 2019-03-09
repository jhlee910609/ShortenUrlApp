package com.example.shortenurlapp.model

import com.example.shortenurlapp.api.Api
import io.reactivex.Single

class NetworkRepositoryImpl(private val api: Api) : Repository {

    override fun getShortenUrl(url: String): Single<ShortenUrl> = api.shortenUrl(url)
        .map { response ->
            response.result
        }
}