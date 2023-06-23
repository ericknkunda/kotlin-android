package com.example.test_resource_files

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities =[CountryModal::class], version =2, exportSchema = false)
abstract class AppDB:RoomDatabase() {
    abstract fun dataObj(): DataObject

    companion object {
        @Volatile
        private var INSTANCE: AppDB? = null

        fun getDatabase(context: Context): AppDB {

            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDB::class.java,
                    "app_database"
                ).allowMainThreadQueries().build()
            }
            return INSTANCE!!
            INSTANCE
        }
        }
    }
