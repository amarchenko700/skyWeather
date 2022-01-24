package com.skysoft.skyweather.view.citieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentCitiesListBinding
import com.skysoft.skyweather.model.CITY_KEY
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.IS_RUSSIAN_KEY
import com.skysoft.skyweather.model.WEATHER_KEY
import com.skysoft.skyweather.view.AppStateListCities
import com.skysoft.skyweather.view.weathercard.WeatherFragment

class ListCitiesFragment : Fragment(), OnItemClickListener {

    private val adapter: CitiesListAdapter by lazy { CitiesListAdapter(this) }
    private var isRussian = true
    private var clickedItem: City? = null

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
    }

    private fun openCityWeatherData(city: City) {

        clickedItem = city

        activity?.run {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container_framelayout,
                    WeatherFragment.newInstance(Bundle().apply {
                        putParcelable(CITY_KEY, city)
                    })
                )
                .addToBackStack(null)
                .commit()
        }
    }
}