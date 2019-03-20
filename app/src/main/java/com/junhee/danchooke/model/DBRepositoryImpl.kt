package com.junhee.danchooke.model

import com.junhee.danchooke.dao.UrlDao

class DBRepositoryImpl(private val dao: UrlDao) : DBRepository {

    var allUrls = dao.getAllUrls()

    override fun insert(shortenUrl: ShortenUrl) {
        dao.insertUrl(shortenUrl)
    }
}