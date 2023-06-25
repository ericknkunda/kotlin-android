package com.example.test_resource_files

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class AllCountries : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var countriesAdapter: CountriesAdapter
     lateinit var countryDbObj:CountryDatabase
     private var onRecyclerViewLoaded:RecyclerViewLoaded? =null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?):
            View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.all_countries, container, false)

        recyclerView =view.findViewById(R.id.countries_recycler)

        val searchView =view.findViewById<SearchView>(R.id.search)
        searchView.queryHint ="search by country name"
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query:String):Boolean{

                return  false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                countriesAdapter.filter.filter(newText)
                return true
            }

        })
        val list = mutableListOf<CountryAttributes>()

        val layoutManager:LinearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager =layoutManager


        countryDbObj =CountryDatabase.getInstance(requireContext())
        val countryInstance =countryDbObj.getCountriesDao()


        val listOf =countryInstance.getCountries()
        Log.d("Data", "List of countries $listOf")
        countriesAdapter =CountriesAdapter(listOf)
        recyclerView.adapter =countriesAdapter
        if(countriesAdapter.itemCount>0) {
            onRecyclerViewLoaded?.onRecyclerViewLoadedListener()
        }


        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment eacMessage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllCountries().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}