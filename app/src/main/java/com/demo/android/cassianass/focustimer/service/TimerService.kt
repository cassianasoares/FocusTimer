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
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.demo.android.cassianass.focustimer.MainActivity
import com.demo.android.cassianass.focustimer.R
import com.demo.android.cassianass.focustimer.model.TimeModel
import com.demo.android.cassianass.focustimer.model.TimerStatus
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_REDEFINED
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_START
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_STOP
import com.demo.android.cassianass.focustimer.util.Constant.NOTIFICATION_CHANNEL_ID
import com.demo.android.cassianass.focustimer.util.Constant.NOTIFICATION_CHANNEL_NAME
import com.demo.android.cassianass.focustimer.util.Constant.NOTIFICATION_ID
import com.demo.android.cassianass.focustimer.util.Constant.convertInMinuteAndSeconds
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TimerService: LifecycleService() {

    private lateinit var notification: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private lateinit var intent: Intent
    private lateinit var pendingIntent: PendingIntent
    private lateinit var countDownTimer : CountDownTimer
    private lateinit var timeModel: TimeModel

    companion object {
        var startTime = MutableLiveData(TimerStatus.START)
        var totalTimeAtual = MutableLiveData<Long>()
        var pausedTime = MutableLiveData<Long>()
        var isInterval = MutableLiveData(false)
        var countInterval= MutableLiveData(0)
        var isFinish = MutableLiveData(false)
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
                    setTimerValues(it)
                    startCounting(timeModel.time)
                }
                ACTION_SERVICE_REDEFINED -> {
                    stopForegroundService()
                    if(startTime.value == TimerStatus.RESUME) {
                        stopCounting()
                    }
                    setTimerValues(it)
                    startTime.value = TimerStatus.START
                }
                ACTION_SERVICE_STOP ->{
                    stopForegroundService()
                    stopCounting()
                    pausedTime.value = 0
                }
                else -> { }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun setTimerValues(intent: Intent?){
        timeModel= intent!!.getParcelableExtra("timeValue")!!
        totalTimeAtual.value = timeModel.time
        pausedTime.value = timeModel.time
        countInterval.value = 0
        isFinish.value = false
    }

    private fun initCountDown(totalTime: Long) {
        countDownTimer = object : CountDownTimer(totalTime, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                pausedTime.value = millisUntilFinished
                setUpdateNotification()
                startTime.value = TimerStatus.RESUME
            }

            override fun onFinish() {
                if (countInterval.value == timeModel.sessionNumber) {
                    stopCounting()
                    isFinish.value = true
                    setUpdateNotification()
                } else {
                    startIntervalCounting()
                }
            }
        }
    }

    private fun setUpdateNotification() {
            if(isInterval.value == true && startTime.value == TimerStatus.RESUME) {
                updateNotificationPeriodically(
                    "Time to relax!",
                    "Take a break and drink some water..."
                )
            }else if (isInterval.value == false && startTime.value == TimerStatus.RESUME){
                updateNotificationPeriodically(
                    "Pomodoro Time:",
                    convertInMinuteAndSeconds(pausedTime.value!!)
                )
            }else {
                updateNotificationPeriodically(
                    "Congratulations!",
                    "You finish your ${convertInMinuteAndSeconds(timeModel.time)} pomodoro session"
                )
            }
    }

    private fun startCounting(value: Long) {
        totalTimeAtual.value = value
        initCountDown(value)
        Log.d("IntervalValue", countInterval.toString())
        countDownTimer.start()
    }

    private fun startIntervalCounting() {
        isInterval.value = if(isInterval.value == false){
            if(timeModel.sessionNumber == 4 && countInterval.value == 3) {
                startCounting(1800000)
            }else{
                startCounting(timeModel.timeInterval)
            }
            countInterval.value= countInterval.value!! + 1
            true
        }else{
            startCounting(timeModel.time)
            false
        }
        Log.d("StatusInterval", isInterval.value!!.toString())
    }

    private fun stopCounting() {
        isInterval.value = false
        countDownTimer.cancel()
        startTime.value = TimerStatus.FINISH

    }


    private fun updateNotificationPeriodically(title: String, text: String) {
        notification.apply {
            setSmallIcon(R.drawable.ic_time)
            setContentTitle(title)
            setContentText(text)
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