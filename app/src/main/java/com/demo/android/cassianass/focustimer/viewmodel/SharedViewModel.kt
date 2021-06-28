package com.demo.android.cassianass.focustimer.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.android.cassianass.focustimer.TimerStatus
import java.util.concurrent.TimeUnit

class SharedViewModel: ViewModel() {

    var timer = MutableLiveData<String>()
    var millisInFuture = MutableLiveData<Long>(90000)
    var pausedTime = MutableLiveData<Long>()
    var porcentage = MutableLiveData<Int>()
    var startTime = MutableLiveData(TimerStatus.START)
    private lateinit var countDownTimer: CountDownTimer


    private fun countDownTimer(millisInFuture: Long) {

        countDownTimer = object : CountDownTimer(millisInFuture, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                pausedTime.value = millisUntilFinished
                Log.d("ValorTImerAtual", pausedTime.value.toString())
                convertInMinuteAndSeconds(millisUntilFinished)
                convertTimeToPorcentage(pausedTime.value!!, 90000)
            }

            override fun onFinish() {
                startTime.value = TimerStatus.START
            }
        }
    }

    fun startCounting() {
        countDownTimer(millisInFuture.value!!)
        countDownTimer.start()
        startTime.value = TimerStatus.RESUME
    }

    fun pauseCounting(){
        countDownTimer.cancel()
        //convertInMinuteAndSeconds(pausedTime.value!!)
        startTime.value = TimerStatus.PAUSE
    }

    fun resumeCounting(){
        countDownTimer(pausedTime.value!!)
        Log.d("MilliFutureDepois", pausedTime.value.toString())
        countDownTimer.start()
        startTime.value = TimerStatus.RESUME
    }

    fun controlStatus(){
        when(startTime.value){
            TimerStatus.START -> startCounting()
            TimerStatus.PAUSE -> resumeCounting()
            TimerStatus.RESUME -> pauseCounting()
        }
        Log.d("Status", startTime.value.toString())
    }

    private fun convertInMinuteAndSeconds(millis: Long){
        timer.value = String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis),
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }

    private fun convertTimeToPorcentage(millisUntilFinished: Long, millisInFuture: Long){
        val timeTotal = millisInFuture /1000
        val timeInt = millisUntilFinished / 1000
        porcentage.value = (timeInt.toInt() * 100) / timeTotal.toInt()
    }

}