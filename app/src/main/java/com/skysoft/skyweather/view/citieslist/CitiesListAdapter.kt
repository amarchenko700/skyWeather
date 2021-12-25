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

    private var data: List<City> = ArrayList<City>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesListViewHolder {
        val binding: ItemCityBinding =
            ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitiesListViewHolder(binding.root)
    }

    fun getItemByPosition(position: Int): City {
        return this.data[position]
    }

    override fun onBindViewHolder(holder: CitiesListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<City>) {
        this.data = data
    }

    inner class CitiesListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(city: City) {
            val binding = ItemCityBinding.bind(itemView)
            binding.nameCityItemCity.text = city.name
            binding.root.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
