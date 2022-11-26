package com.example.stockapp.buysellview

import androidx.lifecycle.*
import com.example.stockapp.BaseApplication
import com.example.stockapp.database.PortfolioDataDao
import com.example.stockapp.database.PortfolioDataEntity
import com.example.stockapp.stockcard.Stock
import com.example.stockapp.stockcard.StockData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.random

class BuySellViewModel : ViewModel(){

    private lateinit var portfolioDao : PortfolioDataDao
    private lateinit var portfolio : LiveData<List<PortfolioDataEntity>>

    fun init(dao: PortfolioDataDao)
    {
        portfolioDao = dao
        portfolio = portfolioDao.getAll().asLiveData()
    }
    fun insertStock(stockData: StockData) {
        viewModelScope.launch(Dispatchers.IO) { portfolioDao.insertOne(PortfolioDataEntity(
            random().toInt(),
            stockData.fullStockName,
            stockData.stockName,
            stockData.stockPrice,
            stockData.stockPriceChange,
            stockData.quantityHolding)) }
    }

    fun getStock(symbol : String) : StockData
    {
        val stock = portfolioDao.findBySymbol(symbol).asLiveData().value
        return StockData(stock?.symbol!!, stock.name!!, stock.price, stock.change, stock.holding )

    }

    fun updateStock(stockData: StockData)
    {
        val stock = getStock(stockData.stockName)
        var quantity = stock.quantityHolding - stockData.quantityHolding
        if (quantity < 0 )
        {
            quantity = 0.0
        }
        val newStockData = StockData(
            stockData.stockName,
            stockData.fullStockName,
            stockData.stockPrice,
            stockData.stockPriceChange,
            quantity
        )
        insertStock(newStockData)
    }

//    fun deleteStock(stockData: StockData?, symbol : String)
//    {
//        portfolioDao.delete()
//    }
}