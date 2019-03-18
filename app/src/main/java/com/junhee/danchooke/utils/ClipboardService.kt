package com.junhee.danchooke.utils

import android.app.*
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
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
    val mManager: ClipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (INTENT_SERVICE_KEY.equals(intent?.action)) {
            mManager.addPrimaryClipChangedListener(this)
            startForegroundService()
        }
        return START_STICKY
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

    fun startForegroundService() {
        val builder = NotificationCompat.Builder(this, "default")
        builder.setSmallIcon(R.drawable.ic_small_noti)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.background))
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, ShortenUrlActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    },
                    0
                )
            )
            .setChannelId(CHANNEL_ID.toString())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(
                NotificationChannel(
                    ClipboardService.CHANNEL_ID.toString(),
                    ClipboardService.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        startForeground(CHANNEL_ID, builder.build())
    }

    fun notifyChannel(shortenUrl: String, originUrl: String) {
        Toast.makeText(
            applicationContext,
            getString(R.string.clipboard_copied, shortenUrl),
            Toast.LENGTH_LONG
        ).apply {
            val layout = view as LinearLayout
            val tv = layout.getChildAt(0) as TextView
            tv.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        }.show()
        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // android O notification channel 대응 코드
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                Companion.CHANNEL_ID.toString(), Companion.CHANNEL_NAME, importance
            )
            notiManager.createNotificationChannel(mChannel)
        }

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID.toString())
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

        notiManager.notify(CHANNEL_ID, builder.build())

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
        stopForeground(true)
    }

    companion object {
        const val CHANNEL_NAME = "danchooke"
        const val CHANNEL_ID = 1234
        const val INTENT_SERVICE_KEY = "clipboard_service_key"
    }
}