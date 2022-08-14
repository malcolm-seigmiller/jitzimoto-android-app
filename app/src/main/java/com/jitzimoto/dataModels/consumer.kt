package com.jitzimoto.dataModels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consumer_table")
data class consumer(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val name: String,
        val country: String,
        val region: String,
)