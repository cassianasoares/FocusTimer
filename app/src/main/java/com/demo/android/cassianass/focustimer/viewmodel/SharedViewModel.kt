package com.demo.android.cassianass.focustimer.viewmodel

import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    var timer = MutableLiveData<Int>()
    var porcentage = MutableLiveData<Int>()

    fun startCounting(millisInFuture: Long, context: Context) {
        val timeTotal = millisInFuture /1000

        object : CountDownTimer(millisInFuture, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timer.value = millisUntilFinished.toInt() / 1000
                porcentage.value = (timer.value!! * 100) / timeTotal.toInt()
            }

            override fun onFinish() {
                Toast.makeText(context, "done!", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

}