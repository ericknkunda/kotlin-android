package com.example.test_resource_files

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.util.Locale

class CountriesAdapter(var countriesList:List<CountryAttributes>):
    RecyclerView.Adapter<CountriesAdapter.CountriesHolder>(), Filterable{

    private var filteredDataList:List<CountryAttributes> =ArrayList(countriesList)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesAdapter.CountriesHolder {
//        TODO("Not yet implemented")
       val inflator:LayoutInflater =LayoutInflater.from(parent.context)
        val componentView:View=inflator.inflate(R.layout.recycler_layout, parent, false)
        val countriedOnHold =CountriesHolder(componentView)
        return countriedOnHold;
    }

    override fun onBindViewHolder(holder: CountriesAdapter.CountriesHolder, position: Int) {
//        TODO("Not yet implemented")
    val attributedCountry = filteredDataList[position]
        holder.name.text =attributedCountry.name.toString()
        holder.countryCapital.text =attributedCountry.capital
        Picasso.get().load(attributedCountry.flag).into(holder.imageView)
//        Picasso.get().load(attributedCountry.countryShape).into(holder.countryImage)
//        holder.code.text =attributedCountry.code.toString()

//        val imageId =attributedCountry.countryFlag
//        holder.imageView.setImageResource(imageId)

    }

    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
        return  filteredDataList.size
    }
    fun updateData(newCountryList: List<CountryAttributes>) {
        countriesList = newCountryList
        notifyDataSetChanged()
    }

    inner class CountriesHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view:View){
            val position =adapterPosition
            if(position !=RecyclerView.NO_POSITION){
                val country =filteredDataList[position]
                showPopup(country)
            }
        }
        private fun showPopup(country: CountryAttributes){
            val inflater = LayoutInflater.from(itemView.context)
            val popupView = inflater.inflate(R.layout.popup, null)

            // Find and set the country name in the popup view
            val countryNameTextView = popupView.findViewById<TextView>(R.id.country_name)
            val countryLabel =popupView.findViewById<TextView>(R.id.popup_view)
            val area:TextView =popupView.findViewById(R.id.area)
            val continent:TextView =popupView.findViewById(R.id.continent)
            val currency:TextView =popupView.findViewById(R.id.currency)
            val independence:TextView =popupView.findViewById(R.id.independency)
            val population:TextView =popupView.findViewById(R.id.population)
            val capital:TextView =popupView.findViewById(R.id.capital)

//            val president:TextView =popupView.findViewById(R.id.president)

            countryNameTextView.text = country.name
            area.text =country.area.toString()
            continent.text =country.continent
            currency.text =country.currency
            val independent =if(country.independence ==1){
                "Yes"
            }else{
                "No"
            }
            independence.text =independent
            population.text =country.population.toString()
            capital.text =country.capital
//            president.text =country.president



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
        
        val name:TextView=itemView.findViewById(R.id.countryNames)
//        val code:TextView =itemView.findViewById(R.id.serial)
        val imageView:ImageView =itemView.findViewById(R.id.flag_image)
        val countryCapital:TextView =itemView.findViewById(R.id.capital)


    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var query = constraint?.toString()?.lowercase(Locale.getDefault())
                val filteredList =if(query.isNullOrBlank()){
                    countriesList
                }
                else{
                    countriesList.filter { item->
                        item.name.lowercase(Locale.getDefault()).contains(query)
                    }
                }

                val result =FilterResults()
                result.values =filteredList
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredDataList =results?.values as? List<CountryAttributes>?: emptyList()
                notifyDataSetChanged()
            }
        }
    }

}