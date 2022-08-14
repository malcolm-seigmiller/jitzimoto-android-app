package com.jitzimoto.dataModels

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "userdetails_table")
data class userdetails(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val name: String,
        val country: String,
        val city:String,
        val broadbiz:String,
        val type : String,
): Parcelable