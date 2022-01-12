package com.skysoft.skyweather.view.citieslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skysoft.skyweather.databinding.ItemCityBinding
import com.skysoft.skyweather.model.Weather
import java.util.*

class CitiesListAdapter(val listener: OnItemClickListener) :
    RecyclerView.Adapter<CitiesListAdapter.CitiesListViewHolder>() {

    private var weatherData: List<Weather> = ArrayList<Weather>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesListViewHolder {
        val binding: ItemCityBinding =
            ItemCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CitiesListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CitiesListViewHolder, position: Int) {
        holder.bind(this.weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    fun setData(data: List<Weather>) {
        this.weatherData = data
        notifyDataSetChanged()
    }

    inner class CitiesListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(weather: Weather) {
            ItemCityBinding.bind(itemView).let {
                it.nameCityItemCity.text = weather.city.name
                it.root.setOnClickListener {
                    listener.onItemClick(weatherData[adapterPosition])
                }
            }
        }
    }
}
