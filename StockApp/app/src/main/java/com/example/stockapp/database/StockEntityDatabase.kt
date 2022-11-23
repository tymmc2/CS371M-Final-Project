package com.example.stockapp.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [StockDataEntity::class], version = 1)
abstract class StockEntityDatabase : RoomDatabase() {
    abstract fun getDao() : StockDataDao


    companion object
    {
        private var instance : StockEntityDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : StockEntityDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(context.applicationContext, StockEntityDatabase::class.java,
                "stock")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: StockEntityDatabase) {
            val stockDao = db.getDao()
            GlobalScope.launch{
                // insert Operations
            }
        }
    }


}