package com.android.danchookE.model

import com.android.danchookE.api.Api
import io.reactivex.Single

class NetworkRepositoryImpl(private val api: Api) : Repository {

    override fun getShortenUrl(url: String): Single<ShortenUrl> {
        return api.shortenUrl(url)
            .map { shortenUrlResponse ->
                shortenUrlResponse.result
            }
    }
}