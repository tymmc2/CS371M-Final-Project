package com.example.stockapp.stockservice

data class StockItem(
    val name : String,
    val currency : String,
    val symbol : String,
    val stockExchange : String,
    val price : Double,
    val avgVolume : Double,
    val volume : Double,
    val dayHigh : Double,
    val dayLow : Double,
    val yearHigh : Double,
    val yearLow : Double,
    val open : Double,
    val previousClose : Double,
    val marketCapitalisation : Double,
    val earningsPerShare : Double,
    val change : Double,
    val changeInPercent : Double,
    val annualYield : Double,
    val annualYieldPercentage : Double)
