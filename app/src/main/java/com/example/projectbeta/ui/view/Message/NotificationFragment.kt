package com.example.projectbeta.ui.view.Message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectbeta.R
import com.example.projectbeta.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)


        return binding.root
    }


}