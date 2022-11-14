package com.example.stockapp.stockcard

import kotlinx.serialization.Serializable

@Serializable
data class StockData (
    var stockName : String,
    var fullStockName: String,
    var stockPrice : Double,
    var stockPriceChange : String
)