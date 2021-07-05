package com.demo.android.cassianass.focustimer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.demo.android.cassianass.focustimer.databinding.FragmentSetTimeBinding
import com.demo.android.cassianass.focustimer.model.TimeModel
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
            sharedViewModel.setNewTimer(TimeModel( checkedChipTimeGroup[0], checkedChipTimeGroup[1], checkedChipSessionGroup))

            Log.d("TimeGroup", TimeModel( checkedChipTimeGroup[0], checkedChipTimeGroup[1], binding.sessionChipGroup.getSelectedSession()).toString())
            findNavController().popBackStack()
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}