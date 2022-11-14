package com.example.stockapp.stockcache

import android.content.Context
import android.content.SharedPreferences
import com.example.stockapp.stockservice.StockItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StockSharedPref(context: Context){

    private val STOCK_DATA_PREF = "stockDataPref"
    private val STOCK_DATA = "stockDataHolding"
    private val WATCHLIST_STOCK_DATA = "stockDataWatchList"
    private val preferences: SharedPreferences = context.getSharedPreferences(STOCK_DATA_PREF, Context.MODE_PRIVATE)
    private var editor = preferences.edit()

    var gson = Gson()


    fun storePortfolioStockData(stockHolding : MutableMap<String, Double>)
    {
        var json = gson.toJson(stockHolding)
        editor.putString(STOCK_DATA, json)
        editor.commit()
    }

    fun getPortfolioStockData() : MutableMap<String, Double>?
    {
        val mapType = object : TypeToken<MutableMap<String, Double>>() {}.type
        return gson.fromJson(preferences.getString(STOCK_DATA, ""), mapType)
    }

}