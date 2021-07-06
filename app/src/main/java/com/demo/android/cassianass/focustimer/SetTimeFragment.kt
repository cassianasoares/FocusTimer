package com.demo.android.cassianass.focustimer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.demo.android.cassianass.focustimer.databinding.FragmentSetTimeBinding
import com.demo.android.cassianass.focustimer.model.TimeModel
import com.demo.android.cassianass.focustimer.service.TimerService
import com.demo.android.cassianass.focustimer.util.Constant
import com.demo.android.cassianass.focustimer.util.ExtensionFunctions.getSelectedSession
import com.demo.android.cassianass.focustimer.util.ExtensionFunctions.getSelectedTime
import com.demo.android.cassianass.focustimer.viewmodel.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SetTimeFragment  : BottomSheetDialogFragment() {

    private var _binding: FragmentSetTimeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetTimeBinding.inflate(inflater, container, false)

        binding.setNewTimeButton.setOnClickListener {
            val checkedChipTimeGroup = binding.timeChipGroup.getSelectedTime()
            val checkedChipSessionGroup = binding.sessionChipGroup.getSelectedSession()

            val timer = TimeModel( checkedChipTimeGroup[0], checkedChipTimeGroup[1], checkedChipSessionGroup)
            sharedViewModel.setNewTimer(timer)
            sendActionCommandToService(timer)

            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun sendActionCommandToService(timeModel: TimeModel){

        Intent(
            requireContext(),
            TimerService::class.java
        ).apply{
            this.putExtra("timeValue", timeModel)
            this.action = Constant.ACTION_SERVICE_REDEFINED
            requireContext().startService(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}