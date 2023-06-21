package com.example.test_resource_files

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="countries")
data class CountryModal(
    @PrimaryKey val id:String,
    val name:String,
    val flag:String
)