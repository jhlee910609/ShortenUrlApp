package com.junhee.danchooke.utils

import android.app.*
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.junhee.danchooke.R
import com.junhee.danchooke.model.Repository
import com.junhee.danchooke.view.ShortenUrlActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject


class ClipboardService : Service(), ClipboardManager.OnPrimaryClipChangedListener {

    private val mFirebase: FirebaseAnalytics by inject()
    private val repository: Repository by inject()
    private val channelId = 12312
    private val channelName = "danchooke"
    val mManager: ClipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mManager.addPrimaryClipChangedListener(this)
        return super.onStartCommand(intent, flags, startId)
    }

    fun getShortenUrl(url: String) {
        repository.getShortenUrl(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                notifyChannel(it.url, url)
            }, {
            })
    }

    fun notifyChannel(shortenUrl: String, originUrl: String) {
        Toast.makeText(
            applicationContext,
            getString(R.string.wait_txt),
            Toast.LENGTH_LONG
        ).show()
        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // android O notification channel 대응 코드
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                channelId.toString(), channelName, importance
            )
            notiManager.createNotificationChannel(mChannel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId.toString())
        val notificationIntent = Intent(
            applicationContext,
            ShortenUrlActivity::class.java
        ).putExtra(INTENT_KEY_URL, originUrl)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val requestID = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            requestID,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        builder.setContentTitle(getString(com.junhee.danchooke.R.string.shorten_url_complete)) // required
            .setContentText(getString(R.string.clipboard_copied, shortenUrl))  // required
            .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true) // 알림 터치시 반응 후 삭제
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setSmallIcon(com.junhee.danchooke.R.drawable.ic_small_noti)
            .setContentIntent(pendingIntent)

        notiManager.notify(0, builder.build())

        copyToClipBoard(shortenUrl) {
            mFirebase.logEvent("from_background", Bundle().apply { putString("url", originUrl) })
        }
    }

    override fun onPrimaryClipChanged() {
        // 웹 URL 인지 검사하기
        val content = mManager.primaryClip?.getItemAt(0)?.text.toString()
        if (content.isURL()) {
            getShortenUrl(content)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mManager.removePrimaryClipChangedListener(this)
    }
}