package com.demo.android.cassianass.focustimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.android.cassianass.focustimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var progress_number = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        updateProgressBar()

        binding.incrButton.setOnClickListener {
            if(progress_number <= 90){
               progress_number += 10
                updateProgressBar()
            }
        }

        binding.decrButton.setOnClickListener {
            if(progress_number >= 10){
                progress_number -= 10
                updateProgressBar()
            }
        }


    }

    private fun updateProgressBar() {
        binding.progressBar.progress = progress_number
        binding.progressTextView.text = "$progress_number%"
    }
}