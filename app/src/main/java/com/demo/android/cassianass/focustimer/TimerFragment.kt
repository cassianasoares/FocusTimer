package com.demo.android.cassianass.focustimer


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.demo.android.cassianass.focustimer.databinding.FragmentTimerBinding
import com.demo.android.cassianass.focustimer.service.TimerService
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_START
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_STOP
import com.demo.android.cassianass.focustimer.viewmodel.SharedViewModel


class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    var timeLive = MutableLiveData<Long>()
    var timeTotal= MutableLiveData<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTimerBinding.inflate(inflater, container, false)

        binding.fragmentTimer = this
        binding.lifecycleOwner = this
        observes()

        binding.timerProgressTextView.setOnClickListener {
            findNavController().navigate(R.id.action_timerFragment_to_setTimeFragment)
        }

        return binding.root
    }

    private fun observes(){
        sharedViewModel.timeModel.observe(viewLifecycleOwner, {newTime ->
            TimerService.timeModel = newTime
        })
        TimerService.pausedTime.observe(viewLifecycleOwner, {atualTime ->
            timeLive.value = atualTime
        })
        timeTotal.value =TimerService.timeModel.time
    }

    fun sendActionCommandToService(){
        //val action = if(status == TimerStatus.FINISH) ACTION_SERVICE_STOP else ACTION_SERVICE_START

        Intent(
            requireContext(),
            TimerService::class.java
        ).apply{
            this.action = ACTION_SERVICE_START
            requireContext().startService(this)
        }
    }

    fun sendActionCommandToServiceStop(){
        //val action = if(status == TimerStatus.FINISH) ACTION_SERVICE_STOP else ACTION_SERVICE_START

        Intent(
            requireContext(),
            TimerService::class.java
        ).apply{
            this.action = ACTION_SERVICE_STOP
            requireContext().startService(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}