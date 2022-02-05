package com.skysoft.skyweather.map

import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentGoogleMapsMainBinding

class MapsFragment : Fragment() {

    private var _binding: FragmentGoogleMapsMainBinding? = null
    private val binding: FragmentGoogleMapsMainBinding
        get() {
            return _binding!!
        }

    private lateinit var map: GoogleMap
    val markers = arrayListOf<Marker>()

    private val callback = OnMapReadyCallback { googleMap ->
        val kiev = LatLng(50.0, 30.0)
        map = googleMap
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(kiev))
        googleMap.setOnMapLongClickListener {
            getAddress(it)
            addMarker(it)
            drawLine()
        }
        //map.isMyLocationEnabled = true
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
                               newLatLng,15f
                        )
                    )
                    addMarker(newLatLng)
                    //map.addMarker(MarkerOptions().position(newLatLng).title("Marker in Cherkasssy"))
                    //addMarker(newLatLng)
                }
            }
        }.start()
    }

}