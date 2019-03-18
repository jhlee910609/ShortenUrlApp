package com.junhee.danchooke.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.junhee.danchooke.R
import org.koin.android.ext.android.inject


abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    lateinit var viewDataBinding: T
    val mFirebase: FirebaseAnalytics by inject()

    abstract val layoutResourceId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)
    }

    private val finishIntervalTime = 2000
    private var backPressedTime: Long = 0

    override fun onBackPressed() {
        var tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (0 <= intervalTime && finishIntervalTime >= intervalTime) {
            android.os.Process.killProcess(android.os.Process.myPid())
        } else {
            backPressedTime = tempTime
            Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show()
        }

    }
}