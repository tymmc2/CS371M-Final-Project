package com.example.stockapp.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "stock")
data class StockDataEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "symbol") val symbol: String?,
    @ColumnInfo(name = "lastTradedPrice") val price : Double,
    @ColumnInfo(name = "percentChange") val change : Double
)