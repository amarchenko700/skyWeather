package com.skysoft.skyweather.view.weathercard

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentWeatherBinding
import com.skysoft.skyweather.model.CITY_KEY
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.WeatherDTO
import com.skysoft.skyweather.view.AppState

class WeatherFragment : Fragment() {

    private var city: City? = null
    private lateinit var viewModel: WeatherViewModel
    private var _binding: FragmentWeatherBinding? = null
    private val binding: FragmentWeatherBinding
        get() {
            return _binding!!
        }
    private var hasInternet = false

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

        hasInternet = checkForInternet(requireContext())

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })

        if (savedInstanceState != null) {
            city = savedInstanceState.getParcelable<City>(CITY_KEY)
        } else {
            city = arguments?.getParcelable<City>(CITY_KEY)?.apply {
                if (hasInternet) {
                    viewModel.getWeather(this.latitude, this.longitude)
                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getText(R.string.text_no_internet),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
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
                            city?.let {
                                viewModel.getWeather(it.latitude, it.longitude)
                            }
                        }
                    )
                }
            }
            is AppState.Loading -> {
                binding.run { loadingLayout.visibility = View.VISIBLE }
            }
            is AppState.SuccessLoadWeather -> {
                appState.let {
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
            unavailableWeather.visibility = View.GONE
            city!!.let {
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
        outState.putParcelable(CITY_KEY, city)
    }

    companion object {
        fun newInstance(bundle: Bundle) = WeatherFragment().apply { arguments = bundle }
    }
}