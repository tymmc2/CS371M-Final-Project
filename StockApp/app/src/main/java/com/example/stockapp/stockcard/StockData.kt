package com.example.stockapp.stockcard

import kotlinx.serialization.Serializable

@Serializable
data class StockData (
    val stockName : String,
    val fullStockName: String,
    val stockPrice : Double,
    val stockPriceChange : String,
    val quantityHolding: Double
)