package com.android.danchookE.utils

import android.app.*
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.android.danchookE.R
import com.android.danchookE.view.ShortenUrlActivity


class ClipboardService : Service(), ClipboardManager.OnPrimaryClipChangedListener {

    val mManager: ClipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreate() {
        super.onCreate()
        mManager.addPrimaryClipChangedListener(this)

    }

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onPrimaryClipChanged() {
        // 웹 URL 인지 검사하기
        val content = mManager.primaryClip?.getItemAt(0)?.text.toString()
        if (content.isURL()) {
            val channelId = "channel"
            val channelName = "Channel Name"
            val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(
                    channelId, channelName, importance
                )
                notiManager.createNotificationChannel(mChannel)
            }

            val builder = NotificationCompat.Builder(applicationContext, channelId)
            val notificationIntent = Intent(applicationContext, ShortenUrlActivity::class.java)
                .putExtra(INTENT_KEY_URL, content)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val requestID = System.currentTimeMillis().toInt()
            val pendingIntent = PendingIntent.getActivity(
                applicationContext,
                requestID,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            builder.setContentTitle("이동 확인") // required
                .setContentText("URL를 단축하시겠습니까?")  // required
                .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_small_noti)
                .setContentIntent(pendingIntent)
            notiManager.notify(0, builder.build())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mManager.removePrimaryClipChangedListener(this)
    }
}