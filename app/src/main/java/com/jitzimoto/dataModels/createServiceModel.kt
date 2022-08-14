package com.jitzimoto.dataModels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_create_service.view.*
import java.util.*

@Parcelize
data class createServiceModel(
        val serviceProvider : String,
        val serviceName : String,
        val serviceType : String,
        val serviceDescription : String,
        val mon : String,
        val tue : String,
        val wed : String,
        val thu : String,
        val fri : String,
        val sat : String,
        val sun : String,
        val hourStart : String,
        val minStart : String,
        val hourEnd : String,
        val minEnd : String,
        val price : String,
        val currency: String,
        val city : String
):Parcelable
//        val mon : Int,
//        val tue : Int,
//        val wed : Int,
//        val thu : Int,
//        val fri : Int,
//        val sat : Int,
//        val sun : Int,
//        val hourStart : Int,
//        val minStart : Int,
//        val hourEnd : Int,
//        val minEnd : Int,
//        val price : Int,
//        val currency: Currency