package com.example.test_resource_files

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [eacMessage.newInstance] factory method to
 * create an instance of this fragment.
 */
class eacMessage : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var recyclerView: RecyclerView
    private lateinit var countriesAdapter: CountriesAdapter
//    private lateinit var

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View= inflater.inflate(R.layout.fragment_eac_message, container, false)

        recyclerView =view.findViewById(R.id.countries_recycler)
        val list = mutableListOf<CountryAttributes>()
        list.add(CountryAttributes(1, "Rwanda", R.drawable.flag_rwanda))
        list.add(CountryAttributes(2, "Burundi",R.drawable.flag_burundi))
        list.add(CountryAttributes(3, "Kenya", R.drawable.flag_kenya))
        list.add(CountryAttributes(4, "China", R.drawable.flag_china))
        list.add(CountryAttributes(5, "Tanzania", R.drawable.flag_kenya))
        list.add(CountryAttributes(6, "Russia", R.drawable.flag_russia))
        list.add(CountryAttributes(7, "Japan", R.drawable.flag_japan))
        list.add(CountryAttributes(8, "Ethiopia", R.drawable.flag_ethiopia))
        list.add(CountryAttributes(9, "England", R.drawable.flag_england))
        list.add(CountryAttributes(10, "Egypt", R.drawable.flag_egypt))


        val layoutManager:LinearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager =layoutManager
        countriesAdapter =CountriesAdapter(list)
        recyclerView.adapter =countriesAdapter
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
            eacMessage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}