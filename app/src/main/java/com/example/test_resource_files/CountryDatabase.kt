package com.example.test_resource_files

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CountryDataObject::class], version = 6, exportSchema = false)
abstract class CountryDatabase:RoomDatabase() {

    abstract fun getCountriesDao():CountryDataObject

    companion object{

        private var instance:CountryDatabase? =null
//        fallbackToDestructiveMigration() .addMigrations(MigratorClass())
        fun getInstance(context: Context):CountryDatabase{
            if (instance ==null){
            instance = Room.databaseBuilder(
                context.applicationContext,
                CountryDatabase::class.java,
                "countriesdatabase"
            ).allowMainThreadQueries().build()
            }
            return  instance!!
            instance
        }
    }

}