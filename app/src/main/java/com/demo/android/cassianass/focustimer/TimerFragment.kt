package com.demo.android.cassianass.focustimer


import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.demo.android.cassianass.focustimer.databinding.FragmentTimerBinding
import com.demo.android.cassianass.focustimer.model.TimerStatus
import com.demo.android.cassianass.focustimer.util.Constant.NOTIFICATION_CHANNEL_ID
import com.demo.android.cassianass.focustimer.util.Constant.NOTIFICATION_ID
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

        sharedViewModel.startTime.observe(viewLifecycleOwner, {value ->
            if (value == TimerStatus.FINISH){
                sendNotification()
            }
        })

        binding.timerProgressTextView.setOnClickListener {
            sharedViewModel.controlStatusWhenCallOptions()
            findNavController().navigate(R.id.action_timerFragment_to_setTimeFragment)
        }

        return binding.root
    }

    private fun sendNotification(){
        val intent = Intent(requireContext(), MainActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(requireContext(),0,intent,0)


        val notification = NotificationCompat.Builder(requireContext(), NOTIFICATION_CHANNEL_ID)
        notification.apply {
            setSmallIcon(R.drawable.ic_time)
            setContentTitle("Congratulations")
            setContentText("You finished your pomodoro!")
            priority = NotificationCompat.PRIORITY_LOW
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }
        with(NotificationManagerCompat.from(requireContext())) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}