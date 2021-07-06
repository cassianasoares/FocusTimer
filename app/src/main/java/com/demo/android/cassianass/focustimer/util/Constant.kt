package com.demo.android.cassianass.focustimer.util

import java.util.concurrent.TimeUnit

object Constant {

    const val ACTION_SERVICE_START = "ACTION_SERVICE_START"
    const val ACTION_SERVICE_STOP = "ACTION_SERVICE_STOP"
    const val ACTION_SERVICE_REDEFINED = "ACTION_SERVICE_REDEFINED"

    const val NOTIFICATION_CHANNEL_ID = "countdown_notification_id"
    const val NOTIFICATION_ID = 124
    const val NOTIFICATION_CHANNEL_NAME = "countdown_notification"

    fun convertInMinuteAndSeconds(millis: Long): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis),
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        )
    }

}