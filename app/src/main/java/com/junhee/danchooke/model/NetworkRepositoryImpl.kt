package com.junhee.danchooke.model

import com.junhee.danchooke.api.Api
import io.reactivex.Single

class NetworkRepositoryImpl(private val api: Api) : Repository {

    override fun getShortenUrl(url: String): Single<ShortenUrl> {
        return api.shortenUrl(url)
            .map { shortenUrlResponse ->
                shortenUrlResponse.result
            }
    }
}