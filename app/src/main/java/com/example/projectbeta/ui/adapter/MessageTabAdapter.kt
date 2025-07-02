package com.example.projectbeta.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectbeta.ui.view.Prediction.CaloriePredictionFragment
import com.example.projectbeta.ui.view.Prediction.MacronutrientsCalculationFragment

class MessageTabAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    private val fragmentList = listOf(
        MacronutrientsCalculationFragment(),
        CaloriePredictionFragment()
    )

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}