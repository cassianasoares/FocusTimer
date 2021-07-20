package com.demo.android.cassianass.focustimer.bindingadapter

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.demo.android.cassianass.focustimer.R
import com.demo.android.cassianass.focustimer.model.TimeModel
import com.demo.android.cassianass.focustimer.model.TimerStatus
import com.demo.android.cassianass.focustimer.util.Constant.convertInMinuteAndSeconds

@BindingAdapter("changeType")
fun ImageButton.changeType(startTime: TimerStatus){
    when(startTime) {
        TimerStatus.START -> {
            this.setImageResource(R.drawable.ic_play)
        }
        else-> {
            this.setImageResource(R.drawable.ic_stop)
        }
    }
}

@BindingAdapter("convertInMinuteAndSeconds")
fun TextView.convertTime(millisAtual: Long) {
    this.text = convertInMinuteAndSeconds(millisAtual)
}

@BindingAdapter("timeCurrent", "timeTotal", requireAll = true)
fun ProgressBar.convertTimeToPorcentage(millisAtual: Long, millisTotal: Long) {
    val timeTotal = millisTotal / 1000
    val timeInt = millisAtual / 1000
    this.progress = (timeInt.toInt() * 100) / timeTotal.toInt()
}

@BindingAdapter("statusInterval", "statusTimer", requireAll = true)
fun TextView.changeStatusSession(status: Boolean, startTime: TimerStatus){
    this.text = (if(status && startTime == TimerStatus.RESUME) {
                    "Break"
                }else{
                when (startTime) {
                    TimerStatus.FINISH -> "Finish"
                    else -> "Focusing"
                }}
            ).toString()
}

@BindingAdapter("currentInterval","totalInterval", requireAll = true)
fun TextView.setIntervalCounting(currentInterval: Int, totalInterval: TimeModel){
    this.text = "${currentInterval} / ${totalInterval.sessionNumber}"
}
