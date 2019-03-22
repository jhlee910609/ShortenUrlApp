package com.junhee.danchooke.viewmodel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.junhee.danchooke.model.NetworkRepositoryImpl

class ShortenUrlViewModelFactory(
    private val netRepo: NetworkRepositoryImpl,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShortenUrlViewModel(netRepo, context) as T
    }
}