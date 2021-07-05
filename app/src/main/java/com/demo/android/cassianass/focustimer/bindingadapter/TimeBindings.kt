package com.demo.android.cassianass.focustimer.bindingadapter

import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.demo.android.cassianass.focustimer.R
import com.demo.android.cassianass.focustimer.model.TimerStatus

@BindingAdapter("changeType")
fun Button.changeType(startTime: TimerStatus){
    when(startTime) {
        TimerStatus.START -> {
            setConfiguration("START", R.color.purple_500)
        }
        TimerStatus.PAUSE -> {
            setConfiguration("RESUME", R.color.purple_200)
        }
        TimerStatus.RESUME -> {
            setConfiguration("PAUSE", R.color.purple_200)
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

