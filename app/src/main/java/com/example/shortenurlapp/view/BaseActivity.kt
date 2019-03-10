package com.example.shortenurlapp.view

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    lateinit var viewDataBinding: T
    abstract val layoutResId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, layoutResId)
    }
}