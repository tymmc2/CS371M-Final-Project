package com.example.stockapp.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDataDao {

    @Query("SELECT * FROM stock")
    fun getAll() : Flow<List<PortfolioDataEntity>>

    @Query("SELECT * FROM stock WHERE name LIKE :queryName")
    fun findByName(queryName : String) : Flow<PortfolioDataEntity>

    @Query("SELECT * FROM stock WHERE symbol = :querySymbol")
    fun findBySymbol(querySymbol : String) : Flow<List<PortfolioDataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertALL(vararg stocks: PortfolioDataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(stock: PortfolioDataEntity)

    @Delete
    fun delete(stock: PortfolioDataEntity)
}