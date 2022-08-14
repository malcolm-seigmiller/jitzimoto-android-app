package com.jitzimoto.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jitzimoto.dataBase.database
import com.jitzimoto.dataModels.consumer
import com.jitzimoto.dataModels.user
import com.jitzimoto.dataModels.userdetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application): AndroidViewModel(application) {

    private var repository: Repository

    //counts users
    val userRowCount: LiveData<Int>
    init {
        val userDao = database.getDatabase(application).dao()
        repository = Repository(userDao)
        userRowCount = repository.userRowCount
    }

    //counts consumers
    val consumerRowCount: LiveData<Int>
    init {
        val userDao = database.getDatabase(application).dao()
        repository = Repository(userDao)
        consumerRowCount = repository.consumerRowCount
    }

    //reads consumer data
    val readConsumerData: LiveData<consumer>
    init {
        val userDao = database.getDatabase(application).dao()
        repository = Repository(userDao)
        readConsumerData = repository.readConsumerData
    }


    //gets user detail
    val readUserDetails: LiveData<userdetails>
    init {
        val userDao = database.getDatabase(application).dao()
        repository = Repository(userDao)
        readUserDetails = repository.readUserDetails
    }

    //
    val checkUserInfo: LiveData<user>
    init {
        val userDao = database.getDatabase(application).dao()
        repository = Repository(userDao)
        checkUserInfo = repository.checkData
    }


    fun addconsumer(man: consumer){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addConsumer(man)
        }
    }

    fun addLogindetails(man: userdetails){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLoginDetails(man)
        }
    }

    fun adduser(man: user){
        viewModelScope.launch(Dispatchers.IO) {
            repository.adduser(man)
        }
    }

    fun deleteUser(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteuser()
        }
    }

    fun deleteDetails(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDetails()
        }
    }


}