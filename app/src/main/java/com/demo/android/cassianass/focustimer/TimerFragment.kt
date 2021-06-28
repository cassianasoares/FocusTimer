package com.demo.android.cassianass.focustimer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.demo.android.cassianass.focustimer.databinding.FragmentTimerBinding
import com.demo.android.cassianass.focustimer.util.ExtensionFunctions.changeType
import com.demo.android.cassianass.focustimer.viewmodel.SharedViewModel

class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTimerBinding.inflate(inflater, container, false)

        observeCounter()
        sharedViewModel.startTime.observe(viewLifecycleOwner, { started ->
            binding.startButton.changeType(started)

            binding.startButton.setOnClickListener {
                sharedViewModel.controlStatus()
            }
        })

        return binding.root
    }

    private fun observeCounter() {
        sharedViewModel.timer.observe(viewLifecycleOwner, { realTime ->
            binding.progressTextView.text = realTime
        })
        sharedViewModel.porcentage.observe(viewLifecycleOwner,{ value ->
            binding.progressBar.progress = value
            Log.d("Porcentagem", value.toString())
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}