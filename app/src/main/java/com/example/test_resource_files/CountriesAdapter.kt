package com.example.test_resource_files

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CountriesAdapter(val countriesList:List<CountryAttributes>): RecyclerView.Adapter<CountriesAdapter.CountriesHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesAdapter.CountriesHolder {
//        TODO("Not yet implemented")
       val inflator:LayoutInflater =LayoutInflater.from(parent.context)
        val componentView:View=inflator.inflate(R.layout.recycler_layout, parent, false)
        val countriedOnHold =CountriesHolder(componentView)
        return countriedOnHold;
    }

    override fun onBindViewHolder(holder: CountriesAdapter.CountriesHolder, position: Int) {
//        TODO("Not yet implemented")
    val attributedCountry =countriesList.get(position)
        holder.serial.text =attributedCountry.serial.toString()
        holder.countryNames.text =attributedCountry.countryName.toString()

    }

    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
        return  countriesList.size
    }


    class CountriesHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val serial:TextView=itemView.findViewById(R.id.serial)
        val countryNames:TextView =itemView.findViewById(R.id.countryNames)

    }

}