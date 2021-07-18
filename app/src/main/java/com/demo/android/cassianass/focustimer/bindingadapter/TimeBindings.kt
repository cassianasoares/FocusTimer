package com.demo.android.cassianass.focustimer.bindingadapter

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.demo.android.cassianass.focustimer.R
import com.demo.android.cassianass.focustimer.model.TimerStatus
import com.demo.android.cassianass.focustimer.util.Constant.convertInMinuteAndSeconds

@BindingAdapter("changeType")
fun Button.changeType(startTime: TimerStatus){
    when(startTime) {
        TimerStatus.START -> {
            setConfiguration("START", R.color.purple_500)
        }
        TimerStatus.RESUME -> {
            setConfiguration("STOP", R.color.purple_200)
        }
        TimerStatus.FINISH-> {
            setConfiguration("RESTART", R.color.teal_200)
        }
    }
}

private fun Button.setConfiguration(text: String, color:Int ) {
    this.text = text
    this.setBackgroundColor(ContextCompat.getColor(context, color))
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

@BindingAdapter("intervalNumber", "statusForVisibility", requireAll = true)
fun TextView.intervalVisibility(intervalNumber: Int, status: TimerStatus){
    this.visibility = if(intervalNumber > 0 && status == TimerStatus.FINISH) View.VISIBLE else View.INVISIBLE
}

