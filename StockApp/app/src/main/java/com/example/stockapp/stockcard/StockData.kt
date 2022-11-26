package com.example.stockapp.stockcard

import kotlinx.serialization.Serializable

@Serializable
data class StockData (
    val stockName : String,
    val fullStockName: String,
    var stockPrice : Double,
    var stockPriceChange : String,
    var quantityHolding: Double
)