package com.demo.android.cassianass.focustimer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimeModel (var time: Long,
                      var timeInterval: Long,
                      var sessionNumber: Int = 0): Parcelable


enum class TimerStatus {
    START, PAUSE, RESUME
}