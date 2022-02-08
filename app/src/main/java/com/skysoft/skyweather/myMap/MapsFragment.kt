package com.skysoft.skyweather.myMap

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentGoogleMapsMainBinding
import com.skysoft.skyweather.model.CITY_KEY
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.view.weathercard.WeatherFragment

class MapsFragment : Fragment() {

    private var _binding: FragmentGoogleMapsMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    val markers = arrayListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.buttonSearch.setOnClickListener {
            search()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                setMyLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showDialogRationale()
            }
            else -> {
                myRequestPermission()
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
                    setMyLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showDialogRationale()
                }
                else -> {

                }
            }
        }
    }

    private fun showWeatherDialog(location: LatLng) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_dialog_show_weather)
            .setMessage(R.string.message_show_weather)
            .setPositiveButton(R.string.show) { _, _ ->
                openWeatherFragment(location)
            }
            .setNegativeButton(R.string.dontShow) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun openWeatherFragment(location: LatLng) {
        activity?.run {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container_framelayout,
                    WeatherFragment.newInstance(Bundle().apply {
                        putParcelable(
                            CITY_KEY,
                            City(
                                binding.textAddress.text.toString(),
                                location.latitude,
                                location.longitude
                            )
                        )
                    })
                )
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showDialogRationale() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.title_dialog_rationale_access_location)
            .setMessage(R.string.explanation_access_location)
            .setPositiveButton(R.string.grant_access) { _, _ ->
                myRequestPermission()
            }
            .setNegativeButton(R.string.deny_access) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    val REQUEST_CODE = 999
    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
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