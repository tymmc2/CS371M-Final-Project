package com.example.stockapp.totalcard

import android.icu.util.CurrencyAmount
import androidx.lifecycle.ViewModel
import com.example.stockapp.stockcard.StockData
import java.math.BigDecimal

class TotalCardViewModel : ViewModel() {
    private lateinit var _data : TotalData
    val data : TotalData
        get() = _data

    fun init() {
        _data = TotalData(BigDecimal(0.0), BigDecimal(0.0))
    }

    fun changeMoney(amount: BigDecimal) {
        data.amount += amount
        data.amtChange = amount
    }
}