package com.example.test_resource_files

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DataObject {
    @Query("select *from countries")

    fun getCountries():List<CountryModal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountries(countriesList:List<CountryModal>)
}