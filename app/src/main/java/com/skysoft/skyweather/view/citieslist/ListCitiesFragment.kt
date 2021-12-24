package com.skysoft.skyweather.view.citieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skysoft.skyweather.R
import com.skysoft.skyweather.databinding.FragmentCitiesListBinding
import com.skysoft.skyweather.model.CitiesRepoImpl
import com.skysoft.skyweather.model.City
import com.skysoft.skyweather.view.CityFragment
import com.skysoft.skyweather.view.viewmodel.ListCitiesViewModel
import com.skysoft.skyweather.viewmodel.AppState

class ListCitiesFragment: Fragment() {

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
    ): View? {
        _binding = FragmentCitiesListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListCitiesViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer<AppState>{ renderData(it) })
        viewModel.emulateRequest()
        adapter = CitiesListAdapter()
        initRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun renderData(appState: AppState){
        when(appState){
            is AppState.Error -> Toast.makeText(requireContext(), appState.error.message, Toast.LENGTH_SHORT).show()
            is AppState.Loading -> Toast.makeText(requireContext(), "${appState.progress}", Toast.LENGTH_SHORT).show()
            is AppState.Success -> Toast.makeText(requireContext(), appState.weatherData, Toast.LENGTH_SHORT).show()
            is AppState.CityCard -> openCityCard(appState.city)
        }
    }

    fun openCityCard(city: City){
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
                    viewModel.openCityCard(item);
                }
            }
        )
        adapter.setData(CitiesRepoImpl.getCities() as List<City>)
    }

    companion object {
        fun newInstance() = ListCitiesFragment()
    }
}