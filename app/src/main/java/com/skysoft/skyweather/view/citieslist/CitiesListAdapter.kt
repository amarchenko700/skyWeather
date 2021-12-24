package com.skysoft.skyweather.view.citieslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skysoft.skyweather.R
import com.skysoft.skyweather.model.City
import java.util.ArrayList

class CitiesListAdapter : RecyclerView.Adapter<CitiesListAdapter.CitiesListViewHolder>(){

    private var data: List<City> = ArrayList<City>()
    private lateinit var clickListener: OnItemClickListener

    fun getSity(position: Int) = data.get(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesListViewHolder {
        return CitiesListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false),
            this
        )
    }

    fun setClickListener(clickListener: OnItemClickListener) {
        this.clickListener = clickListener
    }

    fun getClickListener() = clickListener

    override fun onBindViewHolder(holder: CitiesListViewHolder, position: Int) {
        holder.fillCity(getSity(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getItem(position: Int) = data.get(position)

    fun setData(data: List<City>){
        this.data = data
    }

    interface OnItemClickListener {
        fun onItemClick(item: City?, position: Int)
    }

    class CitiesListViewHolder(itemView: View, adapter: CitiesListAdapter) :
        RecyclerView.ViewHolder(itemView) {

        private var nameCityTextView = itemView.findViewById<TextView>(R.id.name_city_item_city)

        init {

            itemView.setOnClickListener(View.OnClickListener {
                if (adapter.getClickListener() != null) {
                    adapter.getClickListener()
                        .onItemClick(adapter.getItem(adapterPosition), adapterPosition)
                }
            })
        }

        fun fillCity(city: City) {
            nameCityTextView.text = city.name
        }

    }
}
