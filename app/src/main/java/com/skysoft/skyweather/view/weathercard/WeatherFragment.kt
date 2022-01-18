package com.skysoft.skyweather.view.weathercard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentWeatherBinding
import com.skysoft.skyweather.model.CITY_KEY
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.WeatherDTO
import com.skysoft.skyweather.view.AppState

const val WEATHER_KEY = "WEATHER_KEY"

class WeatherFragment : Fragment() {

    private var city: City? = null
    private var weatherDTO: WeatherDTO? = null
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

        if (savedInstanceState != null) {
            weatherDTO = savedInstanceState.getParcelable<WeatherDTO>(WEATHER_KEY)
            city = savedInstanceState.getParcelable<City>(CITY_KEY)
        } else {
            viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
            city = arguments?.getParcelable<City>(CITY_KEY)
            viewModel.getWeather(city!!)
        }

        weatherDTO?.let {
            fillCardWeather(it)
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.run {
                    loadingLayout.visibility = View.GONE
                    tvDesriptionError.text = appState.error.toString()
                    root.snackbarWithAction(
                        getString(R.string.Error), getString(R.string.TryAgain), {
                            viewModel.getWeather(appState.city)
                        }
                    )
                }
            }
            is AppState.Loading -> {
                binding.run { loadingLayout.visibility = View.VISIBLE }
            }
            is AppState.SuccessLoadWeather -> {
                appState.let {
                    weatherDTO = it.weatherDTO
                    fillCardWeather(it.weatherDTO)
                }
            }
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
            unavailableWeather.visibility = View.GONE
            city!!.let{
                cityName.text = it.name
                cityCoordinates.text = "${it.latitude} ${it.longitude}"
            }
            weatherDTO.fact.let {
                feelsLikeValue.text = it.feelsLike.toString()
                temperatureValue.text = it.temp.toString()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(WEATHER_KEY, weatherDTO)
        outState.putParcelable(CITY_KEY, city)
    }

    companion object {
        fun newInstance(bundle: Bundle) = WeatherFragment().apply { arguments = bundle }
    }
}