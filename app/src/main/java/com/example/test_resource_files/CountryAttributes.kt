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
    @ColumnInfo(name ="country_flag") var flag:String,
    @ColumnInfo(name ="area") val area:Double,
    @ColumnInfo(name = "shape") var countryShape:String,
    @ColumnInfo(name ="continent") val continent:String,
    @ColumnInfo(name ="currency") val currency:String,
    @ColumnInfo(name ="independence") val independence:Int,
    @ColumnInfo(name ="population") val population:Int,
    @ColumnInfo(name ="president") val president:String,
    @ColumnInfo(name="capital") val capital:String

    ){

    @Ignore
    constructor(name:String, code:String, flag: String, area: Double, shape:String, continent:String, currency:String, independence: Int, population:Int, president: String , capital: String):
            this(0,name, code,flag, area, shape, continent, currency, independence, population, president, capital)
}


