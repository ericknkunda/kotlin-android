package com.example.test_resource_files


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.FrameLayout
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.File

class MainActivity : AppCompatActivity(), RecyclerViewLoaded {
//    var myButton:Button=Button(this)

    lateinit var countryDatabase: CountryDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_resource)
//        myButton =(Button)
        var framelayout =findViewById<FrameLayout>(R.id.fragment_frame)
        var countries =findViewById<Button>(R.id.countries)
        var otherCountries =findViewById<Button>(R.id.othercountries)


        countryDatabase =CountryDatabase.getInstance(applicationContext)
         val countryInstance =countryDatabase.getCountriesDao()

//        countryInstance.deleteAllCountries()

        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main){
//                insertIntoRoom(countryInstance)
            }
        }
    }
    suspend fun insertIntoRoom(countryDao:CountryDataObject){
        GlobalScope.launch(Dispatchers.IO) {
            try{
                val client = OkHttpClient()
                val request = Request.Builder().url("https://restcountries.com/v3.1/all").build()
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                Log.d("Response ", "Response Body $responseBody")
                val fetchedCountries = mutableListOf<Int>()

                if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                    val jsonArray = JSONArray(responseBody)
                    for(i in 0 until jsonArray.length()){
                        fetchedCountries.add(i)
                    }
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

                        val name = jsonObject.getJSONObject("name").getString("common")
                        val code = jsonObject.getString("cca2")
                        var flag = jsonObject.getJSONObject("flags").getString("png")
                        val counter = fetchedCountries[i]


                        val countryAttributes = CountryAttributes(name, code, flag)
                        countryDao.insertData(countryAttributes)
                        Log.d("object", "Retrieved object ${countryDao.getCountries().get(i)}")
                        println("Index ${fetchedCountries.get(i)}")
                    }
                    withContext(Dispatchers.Main){

                    }

                }
            }catch (e:Exception){
                e.printStackTrace()}
        }
    }

    fun openActivitryTwo(view: View){
        val intent =Intent(this,activity2::class.java)
        startActivity(intent)
    }

    fun openFirstFragment(view: View){
        val fragmentInstance =eacMessage()
        val layout =R.id.fragment_frame
        openFragmentOne(fragmentInstance, layout)
    }

    fun openSecondFragment(view: View){
        val fragment =ClickMeFragment()
        val layoutId =R.id.fragment_frame

        loadFragmentTwo(fragment, layoutId)
    }
    fun openFragmentOne(fragment:Fragment, layoutId:Int){
        var manager: FragmentManager =supportFragmentManager
        var transaction: FragmentTransaction =manager.beginTransaction()
        transaction.replace(layoutId, fragment)
        transaction.commit()

    }

    fun loadFragmentTwo(fragment: Fragment, layoutId: Int){
      var  manager:androidx.fragment.app.FragmentManager = supportFragmentManager
        var transaction:androidx.fragment.app.FragmentTransaction =manager.beginTransaction()
        transaction.replace(layoutId, fragment)
        transaction.commit()
    }

    override fun onRecyclerViewLoadedListener() {
        val searchView: SearchView = findViewById(R.id.search)
        searchView.visibility = View.VISIBLE
    }


}