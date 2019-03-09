package com.example.shortenurlapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shortenurlapp.model.Repository

class ShortenUrlViewModelFactory(private val repo: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShortenUrlViewModel(repo) as T
    }
}