package com.junhee.danchooke.model

import com.junhee.danchooke.api.Api
import io.reactivex.Single

class NetworkRepositoryImpl(private val api: Api) : NetworkRepository {

    override fun getShortenUrl(url: String): Single<ShortenUrl> {
        return api.shortenUrl(url)
            .map {
                it.result
            }
    }
}
