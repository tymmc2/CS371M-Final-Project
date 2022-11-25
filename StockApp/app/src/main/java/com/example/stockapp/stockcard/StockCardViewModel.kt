package com.example.stockapp.stockcard

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.stockapp.stockcache.StockSharedPref
import com.example.stockapp.ui.home.HomeFragment

class StockCardViewModel : ViewModel() {
    private lateinit var _data : ArrayList<StockData>
    val data : List<StockData>
    get() = _data

    fun init() {
        _data = ArrayList()
    }

    fun addList(list: List<StockData>) {
        _data.clear()
        _data.addAll(list)
    }

    fun addItem(item: StockData) {
        _data.add(item)
    }

    fun removeItem(item: StockData) {
        _data.remove(item)
    }
}