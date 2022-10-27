package com.example.stockapp.stockcard

import androidx.lifecycle.ViewModel

class StockCardViewModel : ViewModel() {
    private lateinit var _data : MutableList<StockData>
    val data : List<StockData>
    get() = _data

    fun init() {
        _data = mutableListOf();
    }
}