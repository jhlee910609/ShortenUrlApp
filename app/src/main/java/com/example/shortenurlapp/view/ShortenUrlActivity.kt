package com.example.shortenurlapp.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.shortenurlapp.R
import com.example.shortenurlapp.viewmodel.ShortenUrlViewModel
import com.example.shortenurlapp.viewmodel.ShortenUrlViewModelFactory
import org.koin.android.ext.android.inject

class ShortenUrlActivity() : BaseActivity<ActivityShortenUrlBinding>() {
    override val layoutResId: Int = R.layout.activity_shorten_url
    private val shortenUrlViewModelFactory: ShortenUrlViewModelFactory by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shorten_url)

        val shortenUrlViewModel =
            ViewModelProviders.of(this, shortenUrlViewModelFactory).get(ShortenUrlViewModel::class.java)

        shortenUrlViewModel.clickConvert.observe(this, Observer {
            shortenUrlViewModel.getShortenUrl(viewDataBinding.)
        }

        )

    }
}
