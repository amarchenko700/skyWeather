package com.skysoft.skyweather.model

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.skysoft.skyweather.R

fun openFragment(activity: FragmentActivity?, fragmentToOpen: Fragment, addToBackStack: Boolean) {
    activity?.let {
        val transaction = it.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_framelayout, fragmentToOpen)
        if (addToBackStack) transaction.addToBackStack(null)
        transaction.commit()
    }
}

fun showDialogRationale(
    context: Context,
    callbackAccessGranted: () -> Unit,
    title: String,
    message: String
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.grant_access) { _, _ ->
            callbackAccessGranted()
        }
        .setNegativeButton(R.string.deny_access) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}