package com.demo.android.cassianass.focustimer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.android.cassianass.focustimer.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.FocusTimerTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}