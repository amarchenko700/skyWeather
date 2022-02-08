package com.skysoft.skyweather.myMap

import android.Manifest
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentGoogleMapsMainBinding
import com.skysoft.skyweather.model.CITY_KEY
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.openFragment
import com.skysoft.skyweather.model.showDialogRationale
import com.skysoft.skyweather.view.BaseFragment
import com.skysoft.skyweather.view.weathercard.WeatherFragment

class MapsFragment :
    BaseFragment<FragmentGoogleMapsMainBinding>(FragmentGoogleMapsMainBinding::inflate) {

    private lateinit var map: GoogleMap
    val markers = arrayListOf<Marker>()

    private val callbackAccessGranted = {
        checkPermissionForSetMyLocation()
    }

    private val launcherPermissionForMyLocation =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { it ->
            if (it) {
                setMyLocation()
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                    showDialogRationale(
                        requireContext(),
                        callbackAccessGranted,
                        getString(R.string.title_dialog_rationale_access_location),
                        getString(R.string.explanation_access_location)
                    )
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.buttonSearch.setOnClickListener {
            search()
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val kiev = LatLng(50.0, 30.0)
        map = googleMap
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kiev))
        googleMap.setOnMapLongClickListener {
            getAddress(it)
            addMarker(it)
            drawLine()
            showWeatherDialog(it)
        }
        checkPermissionForSetMyLocation()
        map.uiSettings.isZoomControlsEnabled = true
    }

    private fun setMyLocation() {
        map.isMyLocationEnabled = true
    }

    private fun checkPermissionForSetMyLocation() {
        launcherPermissionForMyLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun showWeatherDialog(location: LatLng) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_dialog_show_weather)
            .setMessage(R.string.message_show_weather)
            .setPositiveButton(R.string.show) { _, _ ->
                openFragment(
                    activity,
                    WeatherFragment.newInstance(Bundle().apply {
                        putParcelable(
                            CITY_KEY,
                            City(
                                binding.textAddress.text.toString(),
                                location.latitude,
                                location.longitude
                            )
                        )
                    }),
                    true
                )
            }
            .setNegativeButton(R.string.dontShow) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun addMarker(location: LatLng) {
        val marker = map.addMarker(
            MarkerOptions().position(location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
        )
        markers.add(marker!!)
    }

    private fun drawLine() {
        val lastIndex = markers.size
        if (lastIndex > 1) {
            map.addPolyline(
                PolylineOptions().add(
                    markers[lastIndex - 1].position,
                    markers[lastIndex - 2].position
                ).color(Color.RED)
                    .width(5f)
            )
        }
    }

    private fun getAddress(location: LatLng) {
        Thread {
            val geocoder = Geocoder(requireContext())
            val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (listAddress.count() > 0) {
                requireActivity().runOnUiThread {
                    binding.textAddress.text = listAddress[0].getAddressLine(0)
                }
            }
        }.start()
    }

    private fun search() {
        Thread {
            val geocoder = Geocoder(requireContext())
            val listAddress = geocoder.getFromLocationName(binding.searchAddress.text.toString(), 1)
            if (listAddress.count() > 0) {
                requireActivity().runOnUiThread {
                    val newLatLng = LatLng(
                        listAddress[0].latitude,
                        listAddress[0].longitude
                    )
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            newLatLng, 15f
                        )
                    )
                    addMarker(newLatLng)
                }
            }
        }.start()
    }

}