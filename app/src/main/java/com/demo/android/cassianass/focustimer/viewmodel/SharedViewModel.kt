package com.demo.android.cassianass.focustimer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.android.cassianass.focustimer.model.TimeModel


class SharedViewModel: ViewModel() {

    var timeModel = MutableLiveData<TimeModel>()

    fun setNewTimer(newTime: TimeModel){
        timeModel.value = newTime
    }

}