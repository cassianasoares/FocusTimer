package com.demo.android.cassianass.focustimer.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.demo.android.cassianass.focustimer.R
import com.demo.android.cassianass.focustimer.databinding.FragmentTimerBinding
import com.demo.android.cassianass.focustimer.model.TimeModel
import com.demo.android.cassianass.focustimer.model.TimerStatus
import com.demo.android.cassianass.focustimer.service.TimerService
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_REDEFINED
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_START
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_STOP
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class TimerFragment : Fragment() {

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    var timeModel = TimeModel(1500000, 300000, 1)
    var timeLive = MutableLiveData(timeModel.time)
    var timeTotal= MutableLiveData(timeModel.time)
    var startTime = MutableLiveData(TimerStatus.START)
    var startInterval = MutableLiveData(false)
    var interval = MutableLiveData(0)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTimerBinding.inflate(inflater, container, false)

        binding.fragmentTimer = this
        binding.lifecycleOwner = this


        binding.changeTimeButton.setOnClickListener {
            findNavController().navigate(R.id.action_timerFragment_to_setTimeFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<TimeModel>("timer")?.observe(viewLifecycleOwner){ newTime ->
            if (newTime != null) {
                timeModel = newTime
                Log.d("TimeTotalFrag", timeTotal.toString())
            }
        }
        observeService()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeService() {
        TimerService.currentTotalTime.observe(viewLifecycleOwner, { total ->
            timeTotal.value = total
        })
        TimerService.isFinish.observe(viewLifecycleOwner, { statusFinish ->
            if (statusFinish == true) {
                showDialogWhenCompletePomodoro()
            }
        })
        TimerService.pausedTime.observe(viewLifecycleOwner, { atualTime ->
            if (atualTime != null) {
                timeLive.value = atualTime
            } else {
                timeLive.value = timeTotal.value
            }
        })
        TimerService.countInterval.observe(viewLifecycleOwner, { numberInterval ->
            interval.value = numberInterval!!
        })
        TimerService.startTime.observe(viewLifecycleOwner, { status ->
            startTime.value = status
            Log.d("Status", status.toString())
        })
        TimerService.isInterval.observe(viewLifecycleOwner, { intervalStatus ->
            startInterval.value = intervalStatus
            Log.d("Status", intervalStatus.toString())
        })
    }

    fun showCancelCurrentTimeAlertDialog(status: TimerStatus){
        if (status == TimerStatus.RESUME) {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Do you really want to shop the current timer?")
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Continue") { dialog, _ ->
                    buttonActionCommandToService(status)
                    dialog.dismiss()
                }
                .show()
        }else{
            buttonActionCommandToService(status)
        }
    }

    private fun showDialogWhenCompletePomodoro(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Congratulations")
            .setMessage("You finish your pomodoro session!! Do you want restart the session?")
            .setNegativeButton("FINISH"){dialog, _ ->
                sendActionCommandToService(ACTION_SERVICE_REDEFINED)
                dialog.dismiss()
            }
            .setPositiveButton("RESTART"){dialog, _ ->
                sendActionCommandToService(ACTION_SERVICE_START)
                dialog.dismiss()
            }
            .show()
    }

    private fun buttonActionCommandToService(status: TimerStatus){
        when(status){
            TimerStatus.START -> sendActionCommandToService(ACTION_SERVICE_START)
            TimerStatus.RESUME -> sendActionCommandToService(ACTION_SERVICE_STOP)
            TimerStatus.FINISH -> sendActionCommandToService(ACTION_SERVICE_REDEFINED)
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