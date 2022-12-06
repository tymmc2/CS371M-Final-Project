package com.example.stockapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDataDao {

    @Query("SELECT * FROM stock")
    fun getAll() : Flow<List<PortfolioDataEntity>>

    @Query("SELECT * FROM stock")
    fun getAllSync() : List<PortfolioDataEntity>

    @Query("SELECT * FROM stock WHERE name LIKE :queryName")
    fun findByName(queryName : String) : Flow<PortfolioDataEntity>

    @Query("SELECT * FROM stock WHERE symbol = :querySymbol")
    fun findBySymbol(querySymbol : String) : LiveData<List<PortfolioDataEntity>>

    @Query("SELECT * FROM stock WHERE symbol = :querySymbol")
    fun findBySymbolSync(querySymbol : String) : List<PortfolioDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertALL(vararg stocks: PortfolioDataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(stock: PortfolioDataEntity)

    @Delete
    fun delete(stock: PortfolioDataEntity)
}