package com.skysoft.skyweather.view.contacts

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentContactsBinding
import com.skysoft.skyweather.model.showDialogRationale
import com.skysoft.skyweather.view.BaseFragment

class ContactsFragment : BaseFragment<FragmentContactsBinding>(FragmentContactsBinding::inflate) {

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permitted ->
        val read = permitted.getValue(Manifest.permission.READ_CONTACTS)
        val call = permitted.getValue(Manifest.permission.CALL_PHONE)
        when {
            read && call -> {
                getContacts()
            }
            !shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                showGoSettings()
            }
            !shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) -> {
                showGoSettings()
            }
            else -> {
                showDialogRationale(
                    requireContext(),
                    callbackAccessGranted,
                    getString(R.string.title_dialog_rationale_read_contacts),
                    getString(R.string.explanation_read_contacts)
                )
            }
        }
    }

    private val callbackAccessGranted = {
        myRequestPermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        val resultRead =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
        val resultCall =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
        if (resultRead == PermissionChecker.PERMISSION_GRANTED && resultCall == PermissionChecker.PERMISSION_GRANTED) {
            getContacts()
        } else {
            myRequestPermission()
        }
    }

    private fun myRequestPermission() {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE
            )
        )
    }

    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { checkPermission() }

    private fun showGoSettings() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.need_permission_header))
            .setMessage(
                "${getString(R.string.need_permission_text)} \n" +
                        getString(R.string.need_permission_text_again)
            )
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                openApplicationSettings()
            }
            .setNegativeButton(getString(android.R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                binding.accessContactsDenied.visibility = View.VISIBLE
            }
            .create()
            .show()
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${requireActivity().packageName}")
        )
        settingsLauncher.launch(appSettingsIntent)
    }

    private fun getContacts() {
        binding.accessContactsDenied.visibility = View.GONE
        context?.let { context: Context ->
            val cr = context.contentResolver
            val cursor = cr.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )

            cursor?.let { cursor: Cursor ->
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val indexDisplayName =
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        val indexId = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                        val name = cursor.getString(indexDisplayName)
                        val contactId = cursor.getString(indexId)

                        val view = TextView(context).apply {
                            text = name
                            textSize = 25f
                        }
                        view.setOnClickListener {
                            getNumberFromID(cr, contactId)
                        }
                        binding.containerForContacts.addView(view)
                    }
                }
                cursor.close()
            }
        }
    }

    private fun getNumberFromID(cr: ContentResolver, contactId: String) {
        val phones = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null
        )
        var number: String
        var home: String? = null
        var mobile: String? = null
        var work: String? = null
        var other: String? = null
        phones?.let { cursor ->
            while (cursor.moveToNext()) {
                val indexNumber =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val indexType = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
                number = cursor.getString(indexNumber)
                when (cursor.getInt(indexType)) {
                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> home = number
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> mobile = number
                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> work = number
                    else -> other = number
                }
            }
            phones.close()
        }
        showPhones(home, mobile, work, other)
    }

    private fun showPhones(
        numberHome: String?,
        numberMobile: String?,
        numberWork: String?,
        numberOther: String?
    ) {
        val linear = LinearLayout(requireContext())
        linear.orientation = LinearLayout.VERTICAL
        numberHome?.let { number ->
            linear.addView(createButton(number, getString(R.string.home)))
        }
        numberMobile?.let { number ->
            linear.addView(createButton(number, getString(R.string.mobile)))
        }
        numberWork?.let { number ->
            linear.addView(createButton(number, getString(R.string.work)))
        }
        numberOther?.let { number ->
            linear.addView(createButton(number, getString(R.string.other)))
        }

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.call))
            .setView(linear)
            .create()
            .show()
    }

    private fun createButton(number: String, text: String): Button {
        val button = Button(requireContext())
        button.text = text
        button.setOnClickListener {
            makeCall(number)
        }
        return button
    }

    private fun makeCall(number: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
    }

    companion object {
        fun newInstance() = ContactsFragment()
    }
}