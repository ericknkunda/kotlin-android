package com.example.test_resource_files

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CountryDataObject {
    @Query("SELECT * FROM countriesdb")
    fun getCountries():List<CountryAttributes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertData(countryAttributes: CountryAttributes)

    @Query("DELETE FROM countriesdb")
     fun deleteAllCountries():Unit

     @Query("UPDATE countriesdb SET population = :currentPopulation WHERE country_name =:name ")
     fun updateCountries(name:String, currentPopulation:Int):Integer
}