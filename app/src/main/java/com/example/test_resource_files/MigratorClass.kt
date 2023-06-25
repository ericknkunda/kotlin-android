package com.example.test_resource_files

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigratorClass: Migration(4,6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE countriesdb ADD COLUMN area INTEGER DEFAULT 0")
        database.execSQL("ALTER TABLE countriesdb ADD COLUMN shape STRING DEFAULT NULL")
        database.execSQL("ALTER TABLE countriesdb ADD COLUMN continent STRING DEFAULT NULL")
        database.execSQL("ALTER TABLE countriesdb ADD COLUMN currency STRING DEFAULT NULL")
        database.execSQL("ALTER TABLE countriesdb ADD COLUMN independence INTEGER DEFAULT 0")
        database.execSQL("ALTER TABLE countriesdb ADD COLUMN population INTEGER DEFAULT 0")
        database.execSQL("ALTER TABLE countriesdb ADD COLUMN president STRING DEFAULT NULL")
        database.execSQL("ALTER TABLE countriesdb ADD COLUMN capital STRING DEFAULT NULL")

    }
}