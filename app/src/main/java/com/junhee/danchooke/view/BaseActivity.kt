package com.junhee.danchooke.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.android.inject


abstract class BaseActivity<T: ViewDataBinding>:  AppCompatActivity(){

    lateinit var viewDataBinding: T
    val mFirebase : FirebaseAnalytics by inject()

    abstract val layoutResourceId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = DataBindingUtil.setContentView(this, layoutResourceId)
    }
}