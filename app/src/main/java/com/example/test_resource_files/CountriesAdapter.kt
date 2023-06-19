package com.example.test_resource_files

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
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
        val imageId =attributedCountry.countryFlag
        holder.imageView.setImageResource(imageId)

    }

    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
        return  countriesList.size
    }


    inner class CountriesHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view:View){
            val position =adapterPosition
            if(position !=RecyclerView.NO_POSITION){
                val country =countriesList[position]
                showPopup(country)
            }
        }
        private fun showPopup(country: CountryAttributes){
            val inflater = LayoutInflater.from(itemView.context)
            val popupView = inflater.inflate(R.layout.popup, null)

            // Find and set the country name in the popup view
            val countryNameTextView = popupView.findViewById<TextView>(R.id.popup_view)
            countryNameTextView.text = country.countryName

            // Create a PopupWindow and set its content view
            val popupWindow = PopupWindow(
                popupView,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                true
            )
            // Show the popup at a specific location, or relative to the clicked view
            popupWindow.showAtLocation(itemView, Gravity.CENTER, 0, 0)
        }
        
        val serial:TextView=itemView.findViewById(R.id.serial)
        val countryNames:TextView =itemView.findViewById(R.id.countryNames)
        val imageView:ImageView =itemView.findViewById(R.id.flag_image)

    }

}