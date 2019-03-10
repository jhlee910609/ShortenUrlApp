package com.example.shortenurlapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.shortenurlapp.R
import com.example.shortenurlapp.databinding.ActivityShortenUrlBinding
import com.example.shortenurlapp.utils.copyToClipBoard
import com.example.shortenurlapp.viewmodel.ShortenUrlViewModel
import com.example.shortenurlapp.viewmodel.ShortenUrlViewModelFactory
import org.koin.android.ext.android.inject

class ShortenUrlActivity : BaseActivity<ActivityShortenUrlBinding>() {
    override val layoutResId: Int = R.layout.activity_shorten_url
    private val shortenUrlViewModelFactory: ShortenUrlViewModelFactory by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shorten_url)

        val shortenUrlViewModel =
            ViewModelProviders.of(this, shortenUrlViewModelFactory).get(ShortenUrlViewModel::class.java)

        shortenUrlViewModel.clickConvert.observe(this, Observer {
            shortenUrlViewModel.getShortenUrl(viewDataBinding.urlEditText.text.toString())
        })

        shortenUrlViewModel.error.observe(this, Observer<String> { t ->
            Toast.makeText(this@ShortenUrlActivity, t, Toast.LENGTH_LONG).show()
        })

        shortenUrlViewModel.clickCopyToClipboard.observe(this, Observer { clipUrl ->
            copyToClipBoard(clipUrl) {
                Toast.makeText(
                    this@ShortenUrlActivity,
                    getString(R.string.clipboard_copied, clipUrl),
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        shortenUrlViewModel.clickOpenWeb.observe(this, Observer { clipUrl ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(clipUrl)))
        })

        viewDataBinding.urlEditText.addValidator(shortenUrlViewModel.getUrlValidator(getString(R.string.error_validate_email)))

        viewDataBinding.shortenUrlViewModel = shortenUrlViewModel
        viewDataBinding.setLifecycleOwner(this)

    }
}
