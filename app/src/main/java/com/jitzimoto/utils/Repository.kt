package com.jitzimoto.utils

import androidx.lifecycle.LiveData
import com.jitzimoto.dataBase.Dao
import com.jitzimoto.dataModels.consumer
import com.jitzimoto.dataModels.user
import com.jitzimoto.dataModels.userdetails

class Repository(private val dao: Dao) {

    //counts users
    val userRowCount: LiveData<Int> = dao.getRowCount()

    //counts consumers
    val consumerRowCount: LiveData<Int> = dao.consumerRowCount()

    val readConsumerData: LiveData<consumer> = dao.readConsumerData()

    //get user details
    val readUserDetails: LiveData<userdetails> = dao.readUserDetails()

    //gets user details
    val checkData: LiveData<user> = dao.loginCheck()

    suspend fun addConsumer(man: consumer) {
        dao.addconsumer(man)
    }

    suspend fun addLoginDetails(userdetails: userdetails) {
        dao.addLoginDetails(userdetails)
    }

    suspend fun adduser(user: user) {
        dao.adduser(user)
    }

    suspend fun deleteuser(){
        dao.deleteUser()
    }

    suspend fun deleteDetails(){
        dao.deleteDetails()
    }


}