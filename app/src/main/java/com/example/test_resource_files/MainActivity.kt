package com.example.test_resource_files


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
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
    lateinit  var framelayout:FrameLayout
    lateinit var countries:Button
    lateinit var otherCountries:Button
    lateinit var btnThreeBar:ImageButton
    lateinit var sideMenuButton:Button
//    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        framelayout = findViewById(R.id.fragment_frame)
        countries = findViewById<Button>(R.id.countries)
        otherCountries = findViewById<Button>(R.id.othercountries)
        rotatingAnimation = findViewById(R.id.globe)

        val rotationAnimator = ObjectAnimator.ofFloat(
            rotatingAnimation,
            "rotationY",
            0f,
            360f
        )
        btnThreeBar = findViewById(R.id.three_bar)
        drawerLayout = findViewById(R.id.dlayout)
        navigationView = findViewById(R.id.sidemenus)

        btnThreeBar.setOnClickListener(View.OnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        })
    sideMenuButton =findViewById(R.id.btn_change_theme)
    sideMenuButton.setOnClickListener{
        trigerDialogPopup()
    }


//        val switchMode = findViewById<Switch>(R.id.themeSwitch)
//        sharedPreferences = getSharedPreferences("ThemePreference", MODE_PRIVATE)
        // Retrieve the saved theme preference
//        val savedTheme = sharedPreferences.getInt("Theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // Set the theme switch state based on the saved theme preference
//        switchMode.isChecked = savedTheme == AppCompatDelegate.MODE_NIGHT_YES

        // Apply the saved theme preference
//        AppCompatDelegate.setDefaultNightMode(savedTheme)
    val checkedItem =ThemePreferences(this).darkMode
    navigationView.setNavigationItemSelectedListener { menuItem ->

        when (menuItem.itemId) {
            R.id.settings -> {
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_LONG).show()
                true
            }

            R.id.side_switch -> {
                val switchMode = findViewById<SwitchCompat>(R.id.side_switch)
                switchMode.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }
                true

            }

            else -> false
        }
    }
//        switchMode.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                ThemePreferences(this).darkMode =0
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                ThemePreferences(this).darkMode =1
//            }
//            drawerLayout.closeDrawer(GravityCompat.START)
//        }


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
        translationAnimator.duration = 1500 // Duration for one half of the translation (in milliseconds)
        translationAnimator.repeatCount = ObjectAnimator.INFINITE // Repeat the animation indefinitely
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

    fun trigerDialogPopup(){
    val builder =AlertDialog.Builder(this)
        builder.setTitle("Appearance Theme")
        val themeOptions = arrayOf("Light", "Dark", "System")
        var checkedItem =ThemePreferences(this).darkMode
        builder.setSingleChoiceItems(themeOptions,checkedItem){
            dialog, which->

            when(which){
                0->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()
                    ThemePreferences(this).darkMode =0
                    dialog.dismiss()

                }
                1->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    delegate.applyDayNight()
                    ThemePreferences(this).darkMode  =1
                    dialog.dismiss()
                }
                2->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()
                    ThemePreferences(this).darkMode =2
                    dialog.dismiss()
                }

        }
        }
        val dialog =builder.create()
        dialog.show()

    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.settings -> {
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show()
            }
            else -> false
        }
        drawerLayout.closeDrawer(GravityCompat.START)
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