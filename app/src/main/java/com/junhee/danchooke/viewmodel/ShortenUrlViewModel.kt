package com.junhee.danchooke.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.junhee.danchooke.model.Repository
import com.junhee.danchooke.utils.SingleLiveEvent
import com.rengwuxian.materialedittext.validation.METValidator
import com.rengwuxian.materialedittext.validation.RegexpValidator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ShortenUrlViewModel(private val repo: Repository) : DisposableViewModel() {

    private val _shortenUrl = MutableLiveData<String>()
    private val _error = MutableLiveData<String>()
    private val _clickCopyToClipboard = SingleLiveEvent<String>()
    private val _clickConvert = SingleLiveEvent<Any>()
    private val _clickDelete = SingleLiveEvent<Any>()

    val showResult = MutableLiveData<Boolean>()

    val shortenUrl: LiveData<String> get() = _shortenUrl
    val error: LiveData<String> get() = _error
    val clickCopyToClipboard: LiveData<String> get() = _clickCopyToClipboard
    val clickConvert: LiveData<Any> get() = _clickConvert
    val clickDelete: LiveData<Any> get() = _clickDelete

    fun getUrlValidator(errorMessage: String): METValidator {
        return RegexpValidator(errorMessage, Patterns.WEB_URL.pattern())
    }

    fun getShortenUrl(url: String) {
        if (url.length < 1) {
            _error.value = "URL을 입력해주세요."
        } else {
            addDisposable(
                repo.getShortenUrl(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        showResult.value = true
                        _shortenUrl.value = it.url
                    }, {
                        _error.value = it.message
                    })
            )
        }
    }

    fun clickConvert() {
        _clickConvert.call()
    }

    fun clickCopyToClipboard() {
        _clickCopyToClipboard.value = _shortenUrl.value
    }

    fun clickDelete() {
        _clickDelete.call()
        showResult.value = false
        _shortenUrl.value = ""

    }
}