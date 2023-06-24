package com.example.test_resource_files

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CountryAttributes::class], version = 6, exportSchema = false)
abstract class CountryDatabase:RoomDatabase() {

    abstract fun getCountriesDao():CountryDataObject

    companion object{

        var instance:CountryDatabase? =null
//        fallbackToDestructiveMigration()
        fun getInstance(context: Context):CountryDatabase{
            if (instance ==null){
            instance = Room.databaseBuilder(
                context.applicationContext,
                CountryDatabase::class.java,
                "countriesdatabase"
            ).addMigrations(MigratorClass()).allowMainThreadQueries().build()
            }
            return  instance!!
            instance
        }
    }

}