package com.junhee.danchooke.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.junhee.danchooke.dao.UrlRoomDatabase
import com.junhee.danchooke.model.DBRepositoryImpl
import com.junhee.danchooke.model.NetworkRepositoryImpl
import com.junhee.danchooke.utils.SingleLiveEvent
import com.rengwuxian.materialedittext.validation.METValidator
import com.rengwuxian.materialedittext.validation.RegexpValidator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ShortenUrlViewModel(
    private val netRepo: NetworkRepositoryImpl,
    private var context: Context
) : DisposableViewModel() {

    val dbRepo: DBRepositoryImpl = DBRepositoryImpl(UrlRoomDatabase.getDatabase(context).urlDao())
    var allWords = dbRepo.allUrls


    // 코드 형태 다시 확인
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
        if (url.isEmpty()) {
            _error.value = "URL을 입력해주세요."
        } else {
            addDisposable(
                netRepo.getShortenUrl(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        showResult.value = true
                        _shortenUrl.value = it.shortenUrl
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