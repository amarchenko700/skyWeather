package com.skysoft.skyweather.view.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.skysoft.skyweather.databinding.FragmentContactsBinding
import com.skysoft.skyweather.view.AppStateContacts

class ContactsFragment: Fragment() {

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
        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderData(appStateContacts: AppStateContacts) {
        with(appStateContacts) {
            when (this) {
                is AppStateContacts.Error -> {

                }
                else -> {}
            }
        }
    }

    companion object {
        fun newInstance() = ContactsFragment()
    }
}