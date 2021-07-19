package com.demo.android.cassianass.focustimer


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.demo.android.cassianass.focustimer.databinding.FragmentTimerBinding
import com.demo.android.cassianass.focustimer.model.TimeModel
import com.demo.android.cassianass.focustimer.model.TimerStatus
import com.demo.android.cassianass.focustimer.service.TimerService
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_START
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_STOP
import com.demo.android.cassianass.focustimer.viewmodel.SharedViewModel


class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    var timeLive = MutableLiveData<Long>(90000)
    var timeTotal=  MutableLiveData<Long>(90000)
    lateinit var timeModel: TimeModel
    var startTime = MutableLiveData(TimerStatus.START)
    var startInterval = MutableLiveData(true)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTimerBinding.inflate(inflater, container, false)

        binding.fragmentTimer = this
        binding.lifecycleOwner = this


        binding.timerProgressTextView.setOnClickListener {
            findNavController().navigate(R.id.action_timerFragment_to_setTimeFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sharedViewModel.timeModel.observe(viewLifecycleOwner, {newTime ->
            timeModel = newTime
            Log.d("TimeTotalFrag", timeTotal.toString())
        })
        TimerService.totalTimeAtual.observe(viewLifecycleOwner, {total ->
            timeTotal.value = total
        })
        TimerService.pausedTime.observe(viewLifecycleOwner, {atualTime ->
            if (atualTime != null) {
                timeLive.value = atualTime
            }else{
                timeLive.value = timeTotal.value
            }
        })
        TimerService.startTime.observe(viewLifecycleOwner, { status ->
            startTime.value = status
            Log.d("Status", status.toString())
        })
        TimerService.startInterval.observe(viewLifecycleOwner, { intervalStatus ->
            startInterval.value = intervalStatus
            Log.d("Status", intervalStatus.toString())
        })
        super.onViewCreated(view, savedInstanceState)
    }


    fun buttonStartAction(status: TimerStatus){
        if(status != TimerStatus.RESUME || status == TimerStatus.FINISH){
            sendActionCommandToService(ACTION_SERVICE_START)
        }
    }

    fun buttonStopAction(status: TimerStatus){
        if(status == TimerStatus.RESUME || status == TimerStatus.FINISH) {
            sendActionCommandToService(ACTION_SERVICE_STOP)
        }
    }


    private fun sendActionCommandToService(action: String){
        Intent(
            requireContext(),
            TimerService::class.java
        ).apply{
            this.putExtra("timeValue", timeModel)
            this.action = action
            requireContext().startService(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}