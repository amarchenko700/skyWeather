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
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.model.Weather
import com.skysoft.skyweather.model.WeatherRepositoryImpl
import com.skysoft.skyweather.view.AppState
import com.skysoft.skyweather.view.weathercard.WeatherFragment

class ListCitiesFragment : Fragment(), OnItemClickListener {

    private lateinit var adapter: CitiesListAdapter

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
        _binding = FragmentCitiesListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListCitiesViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getCities()
        adapter = CitiesListAdapter(this)
        initRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.mainViewCitiesList, "Error", Snackbar.LENGTH_LONG)
                    .setAction("Попробовать еще раз") {
                        viewModel.getCities()
                    }.show()
            }
            is AppState.LoadingWeather -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.SuccessLoadCities -> {
                binding.loadingLayout.visibility = View.GONE
            }
            is AppState.OpenCityCard -> openCityCard(appState.city)
            else -> {}
        }
    }

    private fun openCityCard(city: City) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_framelayout, WeatherFragment(Weather(city)))
            .addToBackStack(null)
            .commit()
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.let {
            it.recyclerViewCitiesList.layoutManager = linearLayoutManager
            it.recyclerViewCitiesList.adapter = adapter
        }
        adapter.setData(WeatherRepositoryImpl().getCities() as List<City>)
    }

    companion object {
        fun newInstance() = ListCitiesFragment()
    }

    override fun onItemClick(position: Int) {
        viewModel.openCityCard(adapter.getItemByPosition(position))
    }
}