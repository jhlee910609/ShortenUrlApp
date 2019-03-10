package com.example.shortenurlapp.model

import com.example.shortenurlapp.api.Api
import io.reactivex.Single

class NetworkRepositoryImpl(private val api: Api) : Repository {

    override fun getShortenUrl(url: String): Single<ShortenUrl> {
        return api.shortenUrl(url)
            .map { shortenUrlResponse ->
                shortenUrlResponse.result
            }
    }
}