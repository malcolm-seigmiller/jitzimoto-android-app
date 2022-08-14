package com.jitzimoto.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jitzimoto.dataModels.consumer
import com.jitzimoto.dataModels.user
import com.jitzimoto.dataModels.userdetails

@Database(entities = [user::class, consumer::class, userdetails::class],
        version = 1, exportSchema = false
)
abstract class database: RoomDatabase(){
    abstract fun dao():Dao
    companion object{
        @Volatile
        private var INSTANCE: database? = null
        fun getDatabase(context: Context): database{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        database::class.java,
                        "database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
