package com.example.projectbeta.ui.view.Prediction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.projectbeta.ui.adapter.MessageTabAdapter
import com.example.projectbeta.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class PredictionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_prediction, container, false)

        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        toolbar.setTitle("Prediction")

        viewPager.adapter = MessageTabAdapter(requireActivity())

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Macronutrients"
                1 -> "Calories"
                else -> null
            }
        }.attach()

        return view
    }
}