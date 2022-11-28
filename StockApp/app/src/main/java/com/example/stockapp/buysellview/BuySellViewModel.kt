package com.example.stockapp.buysellview

import androidx.lifecycle.*
import com.example.stockapp.database.PortfolioDataDao
import com.example.stockapp.database.PortfolioDataEntity
import com.example.stockapp.stockcard.StockData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Math.random

class BuySellViewModel : ViewModel(){

    private lateinit var portfolioDao : PortfolioDataDao
    private lateinit var portfolio : LiveData<List<PortfolioDataEntity>?>

    fun init(dao: PortfolioDataDao)
    {
        portfolioDao = dao
        viewModelScope.launch(Dispatchers.IO){
            portfolio = portfolioDao.getAll().asLiveData()
        }
    }

    fun insertStock(stockData: StockData, trade: String)
    {
        viewModelScope.launch(Dispatchers.IO)  {
                portfolioDao.insertOne(
                    PortfolioDataEntity(
                        random().toInt(),
                        stockData.fullStockName,
                        stockData.stockName,
                        stockData.stockPrice,
                        stockData.stockPriceChange,
                        stockData.quantityHolding, trade
                    )
                )
            }
    }

    fun getStock(symbol : String, callback : StockValue)
    {
        var stockData : StockData? = null

        val stock = portfolioDao.findBySymbolSync(symbol)
        val stockSymbol = stock[0].symbol
        val stockName = stock[0].name
        var quantity = 0.0
        var price = 0.0
        for (s in stock)
        {
            quantity += s.holding
            price += s.holding * s.price
        }
        price /= quantity
        val priceChange = stock[stock.lastIndex].change


        stockData = StockData(stockSymbol!!, stockName!!, price, priceChange, quantity)
        callback.onSuccess(stockData)
    }



    fun updateStock(stockData: StockData, trade: String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            getStock(stockData.stockName, object : StockValue{
                override fun onSuccess(stock: StockData) {
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

            })

        }


    }

    interface StockValue
    {
        fun onSuccess(stockData: StockData)
    }

//    fun deleteStock(stockData: StockData?, symbol : String)
//    {
//        portfolioDao.delete()
//    }
}