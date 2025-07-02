package com.example.projectbeta.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.projectbeta.ui.view.WelcomeScreen.WelcomeActivity

class PageAdapter (
    activity: WelcomeActivity,
    private val fragments: List<Fragment>
): FragmentStateAdapter(activity){

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}