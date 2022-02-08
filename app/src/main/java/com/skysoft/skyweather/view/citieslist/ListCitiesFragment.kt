package com.skysoft.skyweather.view.citieslist

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentCitiesListBinding
import com.skysoft.skyweather.model.*
import com.skysoft.skyweather.view.AppStateListCities
import com.skysoft.skyweather.view.BaseFragment
import com.skysoft.skyweather.view.weathercard.WeatherFragment

class ListCitiesFragment :
    BaseFragment<FragmentCitiesListBinding>(FragmentCitiesListBinding::inflate),
    OnItemClickListener {

    private val adapter: CitiesListAdapter by lazy { CitiesListAdapter(this) }
    private var isRussian = true
    private var clickedItem: City? = null

    private lateinit var viewModel: ListCitiesViewModel

    private val ap by lazy {
        requireActivity().getPreferences(Context.MODE_PRIVATE)
    }

    private val callbackAccessGranted = {
        myRequestPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            clickedItem = it.getParcelable(WEATHER_KEY)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel = ViewModelProvider(this).get(ListCitiesViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })

        if (savedInstanceState == null) {
            isRussian = ap.getBoolean(KEY_IS_RUSSIAN_PREFERENCES, false)
            showCitisList()
        } else {
            clickedItem = savedInstanceState.getParcelable(WEATHER_KEY)
            isRussian = savedInstanceState.getBoolean(IS_RUSSIAN_KEY)
            if (clickedItem == null) {
                showCitisList()
            } else {
                openCityWeatherData(clickedItem!!)
            }
        }
    }

    fun initView() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL

        binding.let {
            it.listCitiesFAB.setOnClickListener { onFloatActionButtonClick() }
            it.locationFAB.setOnClickListener { checkPermission() }
            it.recyclerViewCitiesList.layoutManager = linearLayoutManager
            it.recyclerViewCitiesList.adapter = adapter
        }
    }

    private fun showCitisList() {
        with(binding) {
            if (isRussian) {
                listCitiesFAB.setImageResource(R.drawable.ic_russia)
            } else {
                listCitiesFAB.setImageResource(R.drawable.ic_earth)
            }
        }
        viewModel.getCitiesList(isRussian)
    }

    private fun renderData(appStateListCities: AppStateListCities) {
        with(appStateListCities) {
            when (this) {
                is AppStateListCities.LoadedCitiesList -> {
                    adapter.setData(this.citiesList)
                }
                else -> {}
            }
        }
    }

    companion object {
        fun newInstance() = ListCitiesFragment()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putParcelable(WEATHER_KEY, clickedItem)
            putBoolean(IS_RUSSIAN_KEY, isRussian)
        }
    }

    override fun onItemClick(city: City) {
        openCityWeatherData(city)
    }

    private fun onFloatActionButtonClick() {
        isRussian = !isRussian
        showCitisList()
        ap.edit().putBoolean(KEY_IS_RUSSIAN_PREFERENCES, isRussian).apply()
    }

    private fun openCityWeatherData(city: City) {
        clickedItem = city
        openFragment(activity, WeatherFragment.newInstance(Bundle().apply {
            putParcelable(CITY_KEY, city)
        }), true)
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showDialogRationale(
                        requireContext(),
                        callbackAccessGranted,
                        getString(R.string.title_dialog_rationale_access_location),
                        getString(R.string.explanation_access_location)
                    )
                }
                else -> {
                    myRequestPermission()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            when {
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showDialogRationale(
                        requireContext(),
                        callbackAccessGranted,
                        getString(R.string.title_dialog_rationale_access_location),
                        getString(R.string.explanation_access_location)
                    )
                }
                else -> {
                    openApplicationSettings()
                }
            }
        }
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${requireActivity().packageName}")
        )
        settingsLauncher.launch(appSettingsIntent)
    }

    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { checkPermission() }

    val REQUEST_CODE = 999
    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getAddress(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }
    }

    private fun getAddress(location: Location) {
        Thread {
            val geocoder = Geocoder(requireContext())
            val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (listAddress.count() > 0) {
                requireActivity().runOnUiThread {
                    showAddressDialog(listAddress[0].getAddressLine(0), location)
                }
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_address_title)
            .setMessage(address)
            .setPositiveButton(R.string.dialog_address_get_weather) { _, _ ->
                openCityWeatherData(City(address, location.latitude, location.longitude))
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun getLocation() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager =
                    it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.getProvider(LocationManager.GPS_PROVIDER)?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD, MIN_DISTANCE, locationListener
                        )
                    }
                } else {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                        getAddress(it)
                    }
                }
            }
        }
    }
}