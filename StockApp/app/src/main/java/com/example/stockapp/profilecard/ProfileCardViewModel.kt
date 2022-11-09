package com.example.stockapp.profilecard

import androidx.lifecycle.ViewModel
import com.example.stockapp.stockcard.StockData

class ProfileCardViewModel : ViewModel() {
    private lateinit var _data : MutableList<ProfileData>;
    val data : List<ProfileData>
    get() = _data;

    fun init() {
        _data = mutableListOf()
    }

    fun add(data : ProfileData) {
        _data.add(data)
    }
}