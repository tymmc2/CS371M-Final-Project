package com.example.stockapp.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface StockDataDao {

    @Query("SELECT * FROM stock")
    fun getAll() : List<StockDataEntity>

    @Query("SELECT * FROM stock WHERE name LIKE :queryName")
    fun findByName(queryName : String) : StockDataEntity

    @Query("SELECT * FROM stock WHERE symbol LIKE :querySymbol")
    fun findBySymbol(querySymbol : String) : StockDataEntity

    @Insert
    fun insertALL(vararg stocks: StockDataEntity)

    @Insert
    fun insertOne(stock: StockDataEntity)

    @Delete
    fun delete(stock: StockDataEntity)
}