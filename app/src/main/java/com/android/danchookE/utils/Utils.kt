package com.android.danchookE.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.webkit.URLUtil
import androidx.annotation.UiThread
import androidx.core.content.getSystemService
import com.android.danchookE.R

@UiThread
fun Context.copyToClipBoard(content: String, callback: (() -> Unit)? = null) {
    val clip = ClipData.newPlainText(resources.getString(R.string.clip_label), content)
    val clipboard: ClipboardManager? = getSystemService()
    clipboard?.primaryClip = clip
    callback?.invoke()
}

fun String.isURL(): Boolean {
    if (URLUtil.isHttpUrl(this) or URLUtil.isHttpsUrl(this))
        return true
    else
        return false
}