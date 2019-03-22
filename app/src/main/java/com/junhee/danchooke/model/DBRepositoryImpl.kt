package com.junhee.danchooke.model

import androidx.lifecycle.LiveData
import com.junhee.danchooke.dao.UrlDao
import io.reactivex.Flowable

class DBRepositoryImpl(private val dao: UrlDao) : DBRepository {
    override fun getAllUrls(): LiveData<List<ShortenUrl>> {
        return dao.getAllUrls()
    }

    override fun insert(shortenUrl: ShortenUrl) {
        dao.insertUrl(shortenUrl)
    }


}