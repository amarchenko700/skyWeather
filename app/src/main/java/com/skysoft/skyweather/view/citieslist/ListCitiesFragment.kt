package com.skysoft.skyweather.view.citieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentCitiesListBinding
import com.skysoft.skyweather.model.Weather
import com.skysoft.skyweather.view.AppState
import com.skysoft.skyweather.view.weathercard.WEATHER_KEY
import com.skysoft.skyweather.view.weathercard.WeatherFragment

private const val IS_RUSSIAN_KEY = "IS_RUSSIAN_KEY"

class ListCitiesFragment : Fragment(), OnItemClickListener {

    private val adapter: CitiesListAdapter by lazy { CitiesListAdapter(this) }
    private var isRussian = true
    private var clickedItem: Weather? = null

    private var _binding: FragmentCitiesListBinding? = null
    private val binding: FragmentCitiesListBinding
        get() {
            return _binding!!
        }

    private lateinit var viewModel: ListCitiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            clickedItem = it.getParcelable(WEATHER_KEY)
        }
        _binding = FragmentCitiesListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel = ViewModelProvider(this).get(ListCitiesViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })

        if (savedInstanceState == null) {
            sentRequest()
        } else {
            clickedItem = savedInstanceState.getParcelable(WEATHER_KEY)
            isRussian = savedInstanceState.getBoolean(IS_RUSSIAN_KEY)
            if (clickedItem == null) {
                sentRequest()
            } else {
                openWeatherData(clickedItem!!)
            }
        }
    }

    fun initView() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL

        binding.let {
            it.listCitiesFAB.setOnClickListener { onFloatActionButtonClick() }
            it.recyclerViewCitiesList.layoutManager = linearLayoutManager
            it.recyclerViewCitiesList.adapter = adapter
        }
    }

    private fun sentRequest() {
        with(binding) {
            if (isRussian) {
                viewModel.getWeatherFromLocalSourceRus()
                listCitiesFAB.setImageResource(R.drawable.ic_russia)
            } else {
                viewModel.getWeatherFromLocalSourceWorld()
                listCitiesFAB.setImageResource(R.drawable.ic_earth)
            }
        }
        renderData(AppState.Loading(0))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun renderData(appState: AppState) {
        with(appState) {
            when (this) {
                is AppState.Success -> {
                    adapter.setData(this.weatherData)
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

    override fun onItemClick(weather: Weather) {
        openWeatherData(weather)
    }

    private fun onFloatActionButtonClick() {
        isRussian = !isRussian
        sentRequest()
    }

    private fun openWeatherData(weather: Weather) {

        clickedItem = weather

        activity?.run {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container_framelayout,
                    WeatherFragment.newInstance(Bundle().apply {
                        putParcelable(WEATHER_KEY, weather)
                    })
                )
                .addToBackStack(null)
                .commit()
        }
    }
}