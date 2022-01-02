package com.skysoft.skyweather.view.weathercard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.skysoft.skyweather.databinding.FragmentWeatherBinding
import com.skysoft.skyweather.model.Weather
import com.skysoft.skyweather.view.AppState

const val WEATHER_KEY = "WEATHER_KEY"

class WeatherFragment : Fragment() {

    private var weather: Weather? = null
    private lateinit var viewModel: WeatherViewModel
    private var _binding: FragmentWeatherBinding? = null
    private val binding: FragmentWeatherBinding
        get() {
            return _binding!!
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

        if (savedInstanceState != null) {
            weather = savedInstanceState.getParcelable<Weather>(WEATHER_KEY)
        } else {
            weather = arguments?.getParcelable<Weather>(WEATHER_KEY)
            viewModel.getWeather(weather!!.city)
        }

        if (weather != null) {
            fillCardWeather(weather!!)
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.mainViewWeather, "Error", Snackbar.LENGTH_LONG)
                    .setAction("Попробовать еще раз") {
                        viewModel.getWeather(appState.city)
                    }.show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.SuccessLoadWeather -> {
                binding.loadingLayout.visibility = View.GONE
                binding.unavailableWeather.visibility = View.GONE
                appState.weatherData.city.requestsCount = 0
                fillCardWeather(appState.weatherData)
            }
        }
    }

    private fun fillCardWeather(weather: Weather) {
        binding.cityName.text = weather.city.name
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        binding.temperatureValue.text = weather.temperature.toString()
        binding.cityCoordinates.text = "${weather.city.latitude} ${weather.city.longitude}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(WEATHER_KEY, weather)
    }

    companion object {
        fun newInstance(bundle: Bundle): WeatherFragment {
            val fragment = WeatherFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}