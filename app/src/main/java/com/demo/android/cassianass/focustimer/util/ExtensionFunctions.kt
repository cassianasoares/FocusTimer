package com.demo.android.cassianass.focustimer.util

import android.widget.Button
import androidx.core.content.ContextCompat
import com.demo.android.cassianass.focustimer.R
import com.demo.android.cassianass.focustimer.TimerStatus

object ExtensionFunctions {

    fun Button.changeType(startTime: TimerStatus){
        when(startTime) {
            TimerStatus.START -> {
                this.text = "START"
                this.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500))
            }
            TimerStatus.PAUSE -> {
                this.text = "RESUME"
                this.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_200))
            }
            TimerStatus.RESUME -> {
                this.text = "PAUSE"
                this.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_200))
            }

        }
    }

}