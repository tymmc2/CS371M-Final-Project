package com.example.stockapp.stockcache

import com.example.stockapp.stockservice.StockItem

object StockDataCache {

    var stockData : MutableList<StockItem> = mutableListOf()

    fun getStocks() : List<StockItem>
    {
        return stockData
    }

    fun setStocks(stocks : MutableList<StockItem>)
    {
        // clear the old cache and update
        stockData.clear()
        stockData = stocks
    }

    fun getStock(symbol : String?) : StockItem?
    {
        var result : StockItem? = null
        if (!symbol.isNullOrBlank())
        {
            for (stock in stockData)
            {
                if (stock.symbol == symbol)
                {
                    result= stock
                }
            }
        }
        return result

    }



}