package com.skysoft.skyweather.view.weathercard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.google.android.material.snackbar.Snackbar
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentWeatherBinding
import com.skysoft.skyweather.model.*
import com.skysoft.skyweather.view.AppStateWeather

class WeatherFragment : Fragment() {

    private var city: City? = null
    private lateinit var viewModel: WeatherViewModel
    private var _binding: FragmentWeatherBinding? = null
    private val binding: FragmentWeatherBinding
        get() {
            return _binding!!
        }

    val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            viewModel.onReceive(context, intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })

        initView(savedInstanceState)
        requireActivity().let {
            it.registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
            requireActivity().registerReceiver(receiver, IntentFilter(ACTION_ON_LOAD_WEATHER))
            requireActivity().registerReceiver(receiver, IntentFilter(ACTION_ON_ERROR_LOAD_WEATHER))
            requireActivity().registerReceiver(receiver, IntentFilter(ACTION_ON_ERROR_NO_INTERNET))
        }

    }

    private fun initView(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            city = savedInstanceState.getParcelable<City>(CITY_KEY)
        } else {
            city = arguments?.getParcelable<City>(CITY_KEY)?.apply {
                viewModel.getWeather(this, requireContext())
            }
        }
    }

    private fun renderData(appStateWeather: AppStateWeather) {
        when (appStateWeather) {
            is AppStateWeather.Error -> {
                binding.run {
                    unavailableWeather.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
                    tvDesriptionError.text = appStateWeather.error.toString()
                    root.snackbarWithAction(
                        getString(R.string.Error), getString(R.string.TryAgain), {
                            city?.let {
                                viewModel.getWeather(it, requireContext())
                            }
                        }
                    )
                }
            }
            is AppStateWeather.ErrorNoInternet -> {
                binding.unavailableWeather.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    appStateWeather.error,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is AppStateWeather.Loading -> {
                binding.run { loadingLayout.visibility = View.VISIBLE }
            }
            is AppStateWeather.AvailabilityOfTheInternet -> {
                if(appStateWeather.availability){
                    context?.let { ct ->
                        Toast.makeText(ct, getString(R.string.AvailabilityOfTheInternet), Toast.LENGTH_SHORT).show()
                    }
                }else{
                    context?.let { ct ->
                        Toast.makeText(ct, getString(R.string.NotAvailabilityOfTheInternet), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            is AppStateWeather.SuccessLoadWeather -> {
                binding.unavailableWeather.visibility = View.GONE
                appStateWeather.let {
                    fillCardWeather(it.weatherDTO)
                }
            }
            else -> {}
        }
    }

    private fun View.snackbarWithAction(
        textSnackbar: String, textAction: String, funAction: () -> Unit
    ) {
        Snackbar.make(this, textSnackbar, Snackbar.LENGTH_LONG)
            .setAction(textAction) {
                funAction()
            }.show()
    }

    private fun fillCardWeather(weatherDTO: WeatherDTO) {
        binding.run {
            loadingLayout.visibility = View.GONE
            city!!.let {
                cityName.text = it.name
                cityCoordinates.text = "${it.latitude} ${it.longitude}"
            }
            weatherDTO.fact.let {
                feelsLikeValue.text = it.feelsLike.toString()
                temperatureValue.text = it.temp.toString()
            }

            headerIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
            weatherIcon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${weatherDTO.fact.icon}.svg")

        }
    }

    private fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        requireActivity().unregisterReceiver(receiver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CITY_KEY, city)
    }

    companion object {
        fun newInstance(bundle: Bundle) = WeatherFragment().apply { arguments = bundle }
    }
}