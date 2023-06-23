package com.example.test_resource_files

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "countriesdb")
data class CountryAttributes(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "country_name") var name:String,
    @ColumnInfo(name ="country_code") var code:String,
    @ColumnInfo(name ="country_flag") var flag:String){

    @Ignore
    constructor(name:String, code:String, flag: String):this(0,name, code,flag)
}


