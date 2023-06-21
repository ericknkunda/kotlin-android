package com.example.test_resource_files

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class eacMessage : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var countriesAdapter: CountriesAdapter
    val databaseObj = context?.let { AppDB.getDatabase(it) }
    val dataObject = databaseObj?.dataObj()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_eac_message, container, false)

        recyclerView =view.findViewById(R.id.countries_recycler)
        val list = mutableListOf<CountryModal>()

        list.add(CountryModal("1", "Rwanda", "Rwanda-flag"))
        list.add(CountryModal("2", "Burundi", "Burundi-flag"))
        list.add(CountryModal("3", "Kenya", "Kenya-flag"))
        list.add(CountryModal("4", "Uganda", "Uganda-flag"))
        list.add(CountryModal("5", "DR Congo", "DR Congo-flag"))

        dataObject?.insertCountries(list)
        val dataRetrieved =dataObject?.getCountries()

        println("Countries $dataRetrieved")
        val layoutManager:LinearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager =layoutManager

        getAllCpountryCode()
        return view
    }
    fun getAllCpountryCode(){
        GlobalScope.launch(Dispatchers.IO ) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url("https://restcountries.com/v3.1/all").build()
                val response =client.newCall(request).execute()
                val responseBody =response.body?.string()
                val countryCodes =mutableListOf<CountryAttributes>()

                if(response.isSuccessful && !responseBody.isNullOrEmpty()) {

                    val jsonArray = JSONArray(responseBody)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

//                        retrieve vars
                        val name = jsonObject.getJSONObject("name").getString("common")
                        val code = jsonObject.getString("cca2")
                        var flag = jsonObject.getJSONObject("flags").getString("png")

                        val countryAttributes = CountryAttributes(name, code, flag)
                        countryCodes.add(countryAttributes)
                    }
                    withContext(Dispatchers.Main){
                        countriesAdapter = CountriesAdapter(countryCodes)
//                        countriesAdapter.updateData(countryCodes)
                        recyclerView.adapter =countriesAdapter
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

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
            eacMessage().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}