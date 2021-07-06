package com.demo.android.cassianass.focustimer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.demo.android.cassianass.focustimer.MainActivity
import com.demo.android.cassianass.focustimer.R
import com.demo.android.cassianass.focustimer.model.TimeModel
import com.demo.android.cassianass.focustimer.model.TimerStatus
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_START
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_STOP
import com.demo.android.cassianass.focustimer.util.Constant.NOTIFICATION_CHANNEL_ID
import com.demo.android.cassianass.focustimer.util.Constant.NOTIFICATION_CHANNEL_NAME
import com.demo.android.cassianass.focustimer.util.Constant.NOTIFICATION_ID
import com.demo.android.cassianass.focustimer.util.Constant.convertInMinuteAndSeconds

class TimerService: LifecycleService() {

    private lateinit var notification: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private lateinit var intent: Intent
    private lateinit var pendingIntent: PendingIntent


    companion object {
        var startTime = MutableLiveData(TimerStatus.START)
        var timeModel = TimeModel(90000, 1000, 0)
        var pausedTime = MutableLiveData<Long>()
    }

    private var countDownTimer = object : CountDownTimer(timeModel.time, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            pausedTime.value = millisUntilFinished
            updateNotificationPeriodically()
        }

        override fun onFinish() {
            startTime.value = TimerStatus.START
            stopForegroundService()
        }
    }


    override fun onCreate() {
        notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).also {
            notificationManager = it
        }

        intent = Intent(this, MainActivity::class.java)
        pendingIntent = PendingIntent.getActivity(this,0,intent,0)

        createNotificationChannel()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_SERVICE_START -> {
                    startForegroundService()
                    startCounting()
                }
                ACTION_SERVICE_STOP -> {
                    stopForegroundService()
                    stopCounting()
                }
                else -> { }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startCounting() {
        countDownTimer.start()
        startTime.value = TimerStatus.RESUME
    }

    private fun stopCounting() {
        countDownTimer.cancel()
        startTime.value = TimerStatus.FINISH
    }

    fun setNewTimer(newValue: TimeModel) {
        timeModel= newValue
        startTime.value = TimerStatus.START
    }


    fun updateNotificationPeriodically() {
        notification.apply {
            setSmallIcon(R.drawable.ic_time)
            setContentTitle("Pomodoro Time: ")
            setContentText(convertInMinuteAndSeconds(pausedTime.value!!))
            priority = NotificationCompat.PRIORITY_LOW
            setContentIntent(pendingIntent)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }


    private fun startForegroundService() {
        startForeground(
            NOTIFICATION_ID,
            notification.build()
        )
    }

    private fun stopForegroundService() {
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            NOTIFICATION_ID
        )
        stopForeground(true)
        stopSelf()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            channel.lightColor = Color.GRAY
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }
    }
}