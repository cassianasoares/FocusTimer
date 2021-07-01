package com.demo.android.cassianass.focustimer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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

        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = this

        sharedViewModel.startTime.observe(viewLifecycleOwner, { started ->
            binding.startButton.changeType(started)

            binding.startButton.setOnClickListener {
                sharedViewModel.controlStatus()
            }
        })

        binding.progressBar.setOnClickListener {

        }

        binding.timerProgressTextView.setOnClickListener {
            sharedViewModel.controlStatusWhenCallOptions()
            findNavController().navigate(R.id.action_timerFragment_to_setTimeFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}