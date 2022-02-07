package com.skysoft.skyweather.model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.skysoft.skyweather.R

fun openFragment(activity: FragmentActivity?, fragmentToOpen: Fragment, addToBackStack: Boolean) {
    activity?.let{
        val transaction = it.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_framelayout, fragmentToOpen)
        if (addToBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }
}