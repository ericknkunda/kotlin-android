package com.example.test_resource_files

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities =[TestCountryModal::class], version =2, exportSchema = false)
abstract class TestAppDB:RoomDatabase() {
    abstract fun dataObj(): TestDataObject

    companion object {
        @Volatile
        private var INSTANCE: TestAppDB? = null

        fun getDatabase(context: Context): TestAppDB {

            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    TestAppDB::class.java,
                    "app_database"
                ).allowMainThreadQueries().build()
            }
            return INSTANCE!!
            INSTANCE
        }
        }
    }
