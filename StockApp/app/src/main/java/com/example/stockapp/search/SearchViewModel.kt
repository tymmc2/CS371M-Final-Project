package com.example.stockapp.search

import androidx.lifecycle.ViewModel
import com.example.stockapp.stockcard.StockData

class SearchViewModel : ViewModel() {
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