package com.demo.android.cassianass.focustimer.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.android.cassianass.focustimer.model.TimeModel
import com.demo.android.cassianass.focustimer.model.TimerStatus
import java.util.concurrent.TimeUnit


class SharedViewModel: ViewModel() {

    var timer = MutableLiveData<String>()
    var pausedTime = MutableLiveData<Long>()
    var porcentage = MutableLiveData<Int>()
    var startTime = MutableLiveData(TimerStatus.START)
    var timeModel = TimeModel(90000, 1000, 0)
    private lateinit var countDownTimer: CountDownTimer

    init {
        setValuesToProgressAndText(timeModel.time)
    }

    private fun countDownTimer(millisInFuture: Long) {

        countDownTimer = object : CountDownTimer(millisInFuture, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                pausedTime.value = millisUntilFinished
                setValuesToProgressAndText(millisUntilFinished)
            }

            override fun onFinish() {
                startTime.value = TimerStatus.FINISH
            }
        }
    }

    private fun startCounting() {
        countDownTimer(timeModel.time)
        countDownTimer.start()
        startTime.value = TimerStatus.RESUME
    }

    private fun pauseCounting(){
        countDownTimer.cancel()
        //convertInMinuteAndSeconds(pausedTime.value!!)
        startTime.value = TimerStatus.PAUSE
    }

    private fun resumeCounting(){
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
            TimerStatus.FINISH -> startCounting()
        }
        Log.d("Status", startTime.value.toString())
    }

    fun controlStatusWhenCallOptions(){
        if(startTime.value == TimerStatus.RESUME){
            pauseCounting()
        }
    }

    fun setNewTimer(newValue: TimeModel){
        timeModel = newValue
        setValuesToProgressAndText(timeModel.time)
        startTime.value = TimerStatus.START
    }

    private fun setValuesToProgressAndText(timeLive: Long) {
        convertInMinuteAndSeconds(timeLive)
        convertTimeToPorcentage(timeLive, timeModel.time)
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