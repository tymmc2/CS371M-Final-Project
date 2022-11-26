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
    fun insertStock(stockData: StockData, trade : String) {
        var iquantity = getStock(stockData.stockName)?.quantityHolding
        if (iquantity == null)
        {
            iquantity = 0.0
        }
        viewModelScope.launch(Dispatchers.IO) { portfolioDao.insertOne(PortfolioDataEntity(
            random().toInt(),
            stockData.fullStockName,
            stockData.stockName,
            stockData.stockPrice,
            stockData.stockPriceChange,
            stockData.quantityHolding + iquantity!!,trade)) }
    }

    fun getStock(symbol : String) : StockData?
    {
        val stock = portfolioDao.findBySymbol(symbol).asLiveData().value
        if (stock != null)
        {
            val stockSymbol = stock?.get(0)?.symbol
            val stockName = stock?.get(0)?.name
            var quantity : Double = 0.0
            var price : Double = 0.0
            for (s in stock)
            {
                quantity += s.holding
                price += s.holding * s.price
            }
            price /= quantity
            val priceChange = stock[-1].change

            return StockData(stockSymbol!!, stockName!!, price, priceChange, quantity)
        }

        return stock

    }

    fun updateStock(stockData: StockData, trade: String)
    {
        val stock = getStock(stockData.stockName)
        var quantity = 0.0
        if (stock != null)
        {
            quantity = stock.quantityHolding.minus(stockData.quantityHolding)!!
            if (quantity < 0 )
            {
                quantity = 0.0
            }
        }
        val newStockData = StockData(
            stockData.stockName,
            stockData.fullStockName,
            stockData.stockPrice,
            stockData.stockPriceChange,
            quantity
        )
        insertStock(newStockData, trade)
    }

//    fun deleteStock(stockData: StockData?, symbol : String)
//    {
//        portfolioDao.delete()
//    }
}