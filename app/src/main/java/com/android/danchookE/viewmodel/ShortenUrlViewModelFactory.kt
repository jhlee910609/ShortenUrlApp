package com.android.danchookE.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.danchookE.model.Repository

class ShortenUrlViewModelFactory(private val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShortenUrlViewModel(repo) as T
    }
}