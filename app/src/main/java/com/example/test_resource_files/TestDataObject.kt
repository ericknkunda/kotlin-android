package com.example.test_resource_files

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TestDataObject {
    @Query("SELECT * FROM countries")

    fun getCountries():List<TestCountryModal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountries(countriesList:List<TestCountryModal>)
    }
