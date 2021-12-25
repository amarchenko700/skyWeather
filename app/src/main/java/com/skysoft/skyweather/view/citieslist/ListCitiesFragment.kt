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
import com.skysoft.skyweather.model.WeatherRepositoryImpl
import com.skysoft.skyweather.view.citieslist.viewmodel.AppState
import com.skysoft.skyweather.view.cityweather.CityFragment
import com.skysoft.skyweather.view.viewmodel.ListCitiesViewModel

class ListCitiesFragment : Fragment() {

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
        viewModel.getWeatherFromServer()
        adapter = CitiesListAdapter()
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
                        viewModel.getWeatherFromServer()
                    }.show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
            }
            is AppState.openCityCard -> openCityCard(appState.city)
        }
    }

    private fun openCityCard(city: City) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_framelayout, CityFragment(city))
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
        adapter.setClickListener(
            object : CitiesListAdapter.OnItemClickListener {
                override fun onItemClick(item: City?, position: Int) {
                    viewModel.openCityCard(item)
                }
            }
        )
        adapter.setData(WeatherRepositoryImpl().getCities() as List<City>)
    }

    companion object {
        fun newInstance() = ListCitiesFragment()
    }
}