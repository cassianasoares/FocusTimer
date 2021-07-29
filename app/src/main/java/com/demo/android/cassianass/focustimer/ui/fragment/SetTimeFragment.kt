package com.demo.android.cassianass.focustimer.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.demo.android.cassianass.focustimer.databinding.FragmentSetTimeBinding
import com.demo.android.cassianass.focustimer.model.TimeModel
import com.demo.android.cassianass.focustimer.model.TimerStatus
import com.demo.android.cassianass.focustimer.service.TimerService
import com.demo.android.cassianass.focustimer.util.Constant.ACTION_SERVICE_REDEFINED
import com.demo.android.cassianass.focustimer.util.ExtensionFunctions.getSelectedSession
import com.demo.android.cassianass.focustimer.util.ExtensionFunctions.getSelectedTime
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SetTimeFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSetTimeBinding? = null
    private val binding get() = _binding!!

    private lateinit var status : TimerStatus

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetTimeBinding.inflate(inflater, container, false)

        TimerService.startTime.observe(viewLifecycleOwner, { atualStatus ->
            status = atualStatus
        })

        binding.setNewTimeButton.setOnClickListener {
            val checkedChipTimeGroup = binding.timeChipGroup.getSelectedTime()
            val checkedChipSessionGroup = binding.sessionChipGroup.getSelectedSession()

            val timer = TimeModel( checkedChipTimeGroup[0], checkedChipTimeGroup[1], checkedChipSessionGroup)
            findNavController().previousBackStackEntry?.savedStateHandle?.set("timer", timer)

            showCancelCurrentTimeAlertDialog(timer)
        }

        return binding.root
    }


    private fun showCancelCurrentTimeAlertDialog(timeModel: TimeModel){
        if (status == TimerStatus.RESUME) {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Do you really want to shop the current timer?")
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Continue") { dialog, _ ->
                    sendActionCommandToService(timeModel)
                    dialog.dismiss()
                }
                .show()
        }else{
            sendActionCommandToService(timeModel)
        }
        //Estava dando erro, pois estava fechando o fragmento antes de executar a função. Por isso colocar ela só aqui
        findNavController().popBackStack()
    }

    private fun sendActionCommandToService(timeModel: TimeModel){
        Intent(
            requireContext(),
            TimerService::class.java
        ).apply{
            this.putExtra("timeValue", timeModel)
            this.action = ACTION_SERVICE_REDEFINED
            requireContext().startService(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}