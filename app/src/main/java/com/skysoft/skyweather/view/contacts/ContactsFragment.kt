package com.skysoft.skyweather.view.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentContactsBinding
import com.skysoft.skyweather.view.AppStateContacts

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding
        get() {
            return _binding!!
        }

    private lateinit var viewModel: ContactsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        checkPermissions()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun openContacts() {

    }

    private fun checkPermissions() {
        requireContext().let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> openContacts()
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> showDialogRationale()
                else -> myRequestPermission()
            }
        }
    }

    val REQUEST_CODE = 999
    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    private fun showDialogRationale() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_dialog_rationale_read_contacts)
            .setMessage(R.string.explanation_read_contacts)
            .setPositiveButton(R.string.grant_access) { _, _ ->
                myRequestPermission()
            }
            .setNegativeButton(R.string.deny_access) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }


    private fun renderData(appStateContacts: AppStateContacts) {
        with(appStateContacts) {
            when (this) {
                is AppStateContacts.Error -> {
                    //shouldShowRequestPermissionRationale()

                }
                else -> {}
            }
        }
    }

    companion object {
        fun newInstance() = ContactsFragment()
    }
}