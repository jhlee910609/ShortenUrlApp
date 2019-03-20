package com.junhee.danchooke.view

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.junhee.danchooke.databinding.ActivityShortenUrlBinding
import com.junhee.danchooke.utils.ClipboardService
import com.junhee.danchooke.utils.INTENT_KEY_URL
import com.junhee.danchooke.utils.copyToClipBoard
import com.junhee.danchooke.viewmodel.ShortenUrlViewModel
import com.junhee.danchooke.viewmodel.ShortenUrlViewModelFactory
import org.koin.android.ext.android.inject


class ShortenUrlActivity : BaseActivity<ActivityShortenUrlBinding>() {

    override val layoutResourceId: Int = com.junhee.danchooke.R.layout.activity_shorten_url
    private val shortenUrlViewModelFactory: ShortenUrlViewModelFactory by inject()
    private val shortenUrlViewModel by lazy {
        ViewModelProviders.of(this, shortenUrlViewModelFactory).get(ShortenUrlViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shortenUrlViewModel.clickConvert.observe(this, Observer {
            shortenUrlViewModel.getShortenUrl(viewDataBinding.urlEditText.text.toString())
            mFirebase.logEvent("click_convert", Bundle().apply {
                putString("shortenUrl", viewDataBinding.urlEditText.text.toString())
            })
        })

        shortenUrlViewModel.error.observe(this, Observer<String> { t ->
            Toast.makeText(this@ShortenUrlActivity, t, Toast.LENGTH_LONG).show()
        })

        shortenUrlViewModel.clickCopyToClipboard.observe(this, Observer { clipUrl ->
            copyToClipBoard(clipUrl) {
                Toast.makeText(
                    this@ShortenUrlActivity,
                    getString(com.junhee.danchooke.R.string.clipboard_copied, clipUrl),
                    Toast.LENGTH_LONG
                ).show()
            }
            mFirebase.logEvent("click_copy", Bundle().apply {
                putString("shortenUrl", viewDataBinding.urlEditText.text.toString())
            })
        })

        shortenUrlViewModel.clickDelete.observe(this, Observer {
            viewDataBinding.urlEditText.setText("", TextView.BufferType.EDITABLE)
            mFirebase.logEvent("click_delete", Bundle().apply {
                putString("shortenUrl", viewDataBinding.urlEditText.text.toString())
            })
        })

        viewDataBinding.urlEditText.addValidator(
            shortenUrlViewModel.getUrlValidator(
                getString(
                    com.junhee.danchooke.R.string.error_validate_email
                )
            )
        )

        viewDataBinding.shortenUrlViewModel = shortenUrlViewModel
        viewDataBinding.lifecycleOwner = this@ShortenUrlActivity
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            val content = it.getStringExtra(INTENT_KEY_URL).toString()
            viewDataBinding.shortenUrlViewModel?.getShortenUrl(content)
            viewDataBinding.urlEditText.setText(content, TextView.BufferType.EDITABLE)
            mFirebase.logEvent("click_noti", Bundle().apply {
                putString("shortenUrl", content)
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (isForground(this)) {
            stopService(Intent(applicationContext, ClipboardService::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        val intent = Intent(this, ClipboardService::class.java).apply {
            action = ClipboardService.INTENT_SERVICE_KEY
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun isForground(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = manager.getRunningTasks(1)
        if (!tasks.isEmpty()) {
            val topActivity = tasks[0].topActivity
            if (topActivity.packageName != context.packageName)
                return false
        }
        return true
    }
}
