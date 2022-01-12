package com.skysoft.skyweather.view.citieslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skysoft.skyweather.databinding.ItemCityBinding
import com.skysoft.skyweather.model.City
import java.util.*

class CitiesListAdapter(val listener: OnItemClickListener) :
    RecyclerView.Adapter<CitiesListAdapter.CitiesListViewHolder>() {

    private var citiesList: List<City> = ArrayList<City>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesListViewHolder {
        val binding: ItemCityBinding =
            ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitiesListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CitiesListViewHolder, position: Int) {
        holder.bind(this.citiesList[position])
    }

    override fun getItemCount(): Int {
        return citiesList.size
    }

    fun setData(data: List<City>) {
        this.citiesList = data
        notifyDataSetChanged()
    }

    inner class CitiesListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(city: City) {
            ItemCityBinding.bind(itemView).let {
                it.nameCityItemCity.text = city.name
                it.root.setOnClickListener {
                    listener.onItemClick(citiesList[adapterPosition])
                }
            }
        }
    }
}
