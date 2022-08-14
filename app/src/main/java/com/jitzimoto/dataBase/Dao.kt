package com.jitzimoto.dataBase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jitzimoto.dataModels.consumer
import com.jitzimoto.dataModels.user
import com.jitzimoto.dataModels.userdetails

@Dao
interface Dao {

    //add counting function
    @Query("SELECT COUNT(email) FROM user_table")
    fun getRowCount(): LiveData<Int>

    @Query("SELECT COUNT(name) FROM consumer_table")
    fun consumerRowCount(): LiveData<Int>

    @Query("SELECT * FROM consumer_table")
    fun readConsumerData(): LiveData<consumer>
    //live data may not be needed

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addconsumer(man: consumer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLoginDetails(details: userdetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun adduser(user: user)

//    ORDER BY id ASC
    @Query("SELECT * FROM userdetails_table")
    fun readUserDetails(): LiveData<userdetails>
    //not sure about the LiveData part or even the correct data type

    @Query("SELECT * FROM user_table")
    fun loginCheck(): LiveData<user>
    //not sure about stiring

    @Query("DELETE FROM user_table")
    fun deleteUser()

    @Query("DELETE FROM userdetails_table")
    fun deleteDetails()


//    @Update
//    suspend fun updateUser(user: user)

//    @Delete
//    suspend fun deleteCccode(cccode: cccode)

//    @Query("DELETE FROM cccode_table")
//    suspend fun deleteAllCccodes()

}