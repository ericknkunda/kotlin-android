package com.example.test_resource_files


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class MainActivity : AppCompatActivity(), RecyclerViewLoaded, NavigationView.OnNavigationItemSelectedListener {
//    var myButton:Button=Button(this)

    lateinit var countryDatabase: CountryDatabase
    lateinit var rotatingAnimation: ImageView
    lateinit var drawerLayout:DrawerLayout
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
//        myButton =(Button)
        var framelayout = findViewById<FrameLayout>(R.id.fragment_frame)
        var countries = findViewById<Button>(R.id.countries)
        var otherCountries = findViewById<Button>(R.id.othercountries)
        rotatingAnimation = findViewById(R.id.globe)
//        val rotationImage =AnimationUtils.loadAnimation(this, R.anim.rotate_image)
//        rotatingAnimation.startAnimation(rotationImage)
        // Create rotation animation
        // Create rotation animation
        val rotationAnimator = ObjectAnimator.ofFloat(
            rotatingAnimation,
            "rotationY",
            0f,
            360f
        )
        val btnThreeBar = findViewById<ImageButton>(R.id.three_bar)
        drawerLayout = findViewById<DrawerLayout>(R.id.dlayout)
        navigationView = findViewById<NavigationView>(R.id.sidemenus)
//        val switchMode = findViewById<Switch>(R.id.switchMode)

//        if(switchMode.isChecked){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//
//        }else{
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
//
//
//        switchMode.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                // Switch to dark mode
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            } else {
//                // Switch to light mode
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//            recreate()
//        }

        btnThreeBar.setOnClickListener(View.OnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        })

        navigationView.setNavigationItemSelectedListener(this)


        rotationAnimator.duration = 6000 // Duration for one complete rotation (in milliseconds)
        rotationAnimator.repeatCount = ObjectAnimator.INFINITE // Repeat the animation indefinitely
        rotationAnimator.interpolator = LinearInterpolator()

        // Create ObjectAnimator for translation on X-axis
        val translationAnimator = ObjectAnimator.ofFloat(
            rotatingAnimation,
            "translationX",
            -50f,
            50f
        )
        translationAnimator.duration =
            1500 // Duration for one half of the translation (in milliseconds)
        translationAnimator.repeatCount =
            ObjectAnimator.INFINITE // Repeat the animation indefinitely
        translationAnimator.repeatMode = ObjectAnimator.REVERSE // Reverse the translation animation

        // Create AnimatorSet to combine rotation and translation animations
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(rotationAnimator, translationAnimator)

        // Start the combined animation
        animatorSet.start()

        countryDatabase = CountryDatabase.getInstance(applicationContext)
        val countryInstance = countryDatabase.getCountriesDao()

//        countryInstance.deleteAllCountries()

        val isConnected = isNetworkAvaialble(applicationContext)
        if (isConnected) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    updateDb(countryInstance)
                }
            }
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.settings -> {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.switch_mode -> {
                val switchDarkMode = menuItem.actionView?.findViewById<Switch>(R.id.switch_mode)
                switchDarkMode?.setOnCheckedChangeListener { _, isChecked ->
                    val nightMode = if (isChecked) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_NO
                    }
                    AppCompatDelegate.setDefaultNightMode(nightMode)
                    recreate()
                }
            }
            else -> false
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true
    }
    fun isNetworkAvaialble(context:Context):Boolean{
        val connectionManager =context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network =connectionManager.activeNetwork ?: return false
        val networkCapabilities =connectionManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    suspend fun updateDb(countryDao:CountryDataObject){
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
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

                        val name = jsonObject.getJSONObject("name").getString("common")
                        val code = jsonObject.getString("cca2")
                        var flag = jsonObject.getJSONObject("flags").getString("png")
                        val shape =jsonObject.getJSONObject("maps").getString("openStreetMaps")
                        val continent =jsonObject.getString("region")
                        val area =jsonObject.getString("area").toDouble()
                        val population =jsonObject.getString("population").toInt()

                        var currencyname= if(jsonObject.has("currencies")) {
                            jsonObject.getJSONObject("currencies").getJSONObject(jsonObject.getJSONObject("currencies").keys().next()).getString("name")
                        }
                        else{
                            "NA"
                        }
                        val independence =if(jsonObject.has("independence")){
                            jsonObject.getString("independence")
                        }else{
                            "NA"
                        }

                        val president:String =name+"_president"

                        val capital =if(jsonObject.has("capital")){
                            jsonObject.getJSONArray("capital").getString(0)
                        }else{
                            "NA"
                        }
                        var independenceStatus:Int =0;
                        if(independence =="true"){
                            independenceStatus =1
                        }

                        val countryAttributes = CountryAttributes(name, code, flag, area, shape, continent,
                            currencyname, independenceStatus, population, president, capital)
                            countryDao.updateCountries(name, population)
//                        Log.d("object", "Retrieved object ${countryDao.getCountries().get(i)}")
//                        println("Index ${fetchedCountries.get(i)}")
                    }
                    withContext(Dispatchers.Main){

                    }

                }
            }catch (e:Exception){
                e.printStackTrace()}
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
                        val shape =jsonObject.getJSONObject("maps").getString("openStreetMaps")
                        val continent =jsonObject.getString("region")
                        val area =jsonObject.getString("area").toDouble()
                        val population =jsonObject.getString("population").toInt()

                        var currencyname= if(jsonObject.has("currencies")) {
                            jsonObject.getJSONObject("currencies").getJSONObject(jsonObject.getJSONObject("currencies").keys().next()).getString("name")
                        }
                        else{
                            "NA"
                        }
                        val independence =if(jsonObject.has("independence")){
                            jsonObject.getString("independence")
                        }else{
                            "NA"
                        }

                        val president:String =name+"_president"

                        val capital =if(jsonObject.has("capital")){
                            jsonObject.getJSONArray("capital").getString(0)
                        }else{
                            "NA"
                        }
                        var independenceStatus:Int =0;
                        if(independence =="true"){
                            independenceStatus =1
                        }

                        val counter = fetchedCountries[i]


                        val countryAttributes = CountryAttributes(name, code, flag, area, shape, continent,
                            currencyname, independenceStatus, population, president, capital)
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
        val fragmentInstance =AllCountries()
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