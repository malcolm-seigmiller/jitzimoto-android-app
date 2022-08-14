package com.jitzimoto.dataModels

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.LocaleList
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//@Parcelize
@Entity(tableName = "user_table")
data class user(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val email : String,
        val password :String
        //maybe keep more but idk
)//: Parcelable

//trying to get the location
//        val location : String,//not sure about the data type
//        val country : String, //not sure about how I want the country to be stored or what hte data type will be
//        val region : String,//not sure about how I want the region/province/state to be stored or what hte data type will be
//        val city : String,//not sure about the string here
//        val coordinates : Location,
//        val acctype : Boolean,
//        val sertpye : Int
//not sure if i need more

//@Entity(tableName = "user_table")
//data class user(
//        @PrimaryKey(autoGenerate = true)
//        val id: Int,
//        val username : String,
//        val email : String,
//        val password :String,
//        //trying to get the location
////        val location : String,//not sure about the data type
//        val country : String, //not sure about how I want the country to be stored or what hte data type will be
//        val region : String,//not sure about how I want the region/province/state to be stored or what hte data type will be
//        val city : String,//not sure about the string here
////        val coordinates : Location,
//        val acctype : Boolean,
//        val sertpye : Int
//        //not sure if i need more
//)//: Parcelable