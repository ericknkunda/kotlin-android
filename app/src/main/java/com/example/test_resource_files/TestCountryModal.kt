package com.example.test_resource_files

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="countries")
data class TestCountryModal(
    @PrimaryKey var id:Int,
    @ColumnInfo(name = "name") var name:String,
    @ColumnInfo(name="flag") var flag:String
)