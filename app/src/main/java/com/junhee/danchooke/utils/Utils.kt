package com.junhee.danchooke.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.webkit.URLUtil
import androidx.annotation.UiThread
import androidx.core.content.getSystemService
import com.junhee.danchooke.R
import java.text.SimpleDateFormat
import java.util.*

@UiThread
fun Context.copyToClipBoard(content: String, callback: (() -> Unit)? = null) {
    val clip = ClipData.newPlainText(resources.getString(R.string.clip_label), content)
    val clipboard: ClipboardManager? = getSystemService()
    clipboard?.primaryClip = clip
    callback?.invoke()
}

fun getCurrentDate(): String = SimpleDateFormat("yyyy-MM-dd hh:MM:ss", Locale.KOREA).format(Date()).toString()

fun String.isURL(): Boolean = URLUtil.isHttpUrl(this) or URLUtil.isHttpsUrl(this)