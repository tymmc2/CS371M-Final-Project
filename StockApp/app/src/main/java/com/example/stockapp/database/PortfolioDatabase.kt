package com.example.stockapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PortfolioDataEntity::class], version = 1)
abstract class PortfolioDatabase : RoomDatabase() {
    abstract fun getDao() : PortfolioDataDao


    companion object {
        @Volatile
        private var INSTANCE: PortfolioDatabase? = null

        fun getDatabase(context: Context) = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                context.applicationContext,
                PortfolioDatabase::class.java,
                "forageable_database"
            ).build().apply {
                INSTANCE = this
            }
        }

    }


}