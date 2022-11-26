package com.example.stockapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock")
data class PortfolioDataEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "symbol") val symbol: String?,
    @ColumnInfo(name = "lastTradedPrice") val price : Double,
    @ColumnInfo(name = "percentChange") val change : String,
    @ColumnInfo(name = "holding") val holding : Double
)