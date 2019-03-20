package com.junhee.danchooke.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "url_table")
data class ShortenUrl(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "orgUrl")
    val orgUrl: String,

    @ColumnInfo(name = "date")
    val date: String,

    val hash: String?,

    @ColumnInfo(name = "url")
    val url: String
)
