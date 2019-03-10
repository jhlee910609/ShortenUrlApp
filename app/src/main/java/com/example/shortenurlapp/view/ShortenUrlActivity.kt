package com.example.shortenurlapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.shortenurlapp.R
import com.example.shortenurlapp.databinding.ActivityShortenUrlBinding
import com.example.shortenurlapp.utils.ClipboardService
import com.example.shortenurlapp.utils.INTENT_KEY_URL
import com.example.shortenurlapp.utils.copyToClipBoard
import com.example.shortenurlapp.viewmodel.ShortenUrlViewModel
import com.example.shortenurlapp.viewmodel.ShortenUrlViewModelFactory
import org.koin.android.ext.android.inject

class ShortenUrlActivity : BaseActivity<ActivityShortenUrlBinding>() {
    override val layoutResourceId: Int = R.layout.activity_shorten_url
    private val shortenUrlViewModelFactory: ShortenUrlViewModelFactory by inject()
    private val shortenUrlViewModel by lazy {
        ViewModelProviders.of(this, shortenUrlViewModelFactory).get(ShortenUrlViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        shortenUrlViewModel.clickDelete.observe(this, Observer {
            viewDataBinding.urlEditText.setText("", TextView.BufferType.EDITABLE)
        })

        viewDataBinding.urlEditText.addValidator(
            shortenUrlViewModel.getUrlValidator(
                getString(
                    R.string.error_validate_email
                )
            )
        )

        viewDataBinding.shortenUrlViewModel = shortenUrlViewModel
        viewDataBinding.setLifecycleOwner(this)
        startService(Intent(applicationContext, ClipboardService::class.java))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            val content = it.getStringExtra(INTENT_KEY_URL).toString()
            viewDataBinding.shortenUrlViewModel?.getShortenUrl(content)
            viewDataBinding.urlEditText.setText(content, TextView.BufferType.EDITABLE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(applicationContext, ClipboardService::class.java))
    }
}
