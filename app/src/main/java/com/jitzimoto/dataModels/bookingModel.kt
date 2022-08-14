package com.jitzimoto.dataModels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class bookingModel(
    val booker : String,
    val serviceProvider: String,
    val serviceName : String,
    val location : String,
    val comments : String,
    val price : String,
    val day : String,
    val month : String,
    val year : String,
    val hour : String,
    val minute : String
): Parcelable