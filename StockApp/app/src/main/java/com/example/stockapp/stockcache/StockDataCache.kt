package com.example.stockapp.stockcache

import android.text.BoringLayout
import com.example.stockapp.stockservice.StockItem
import yahoofinance.Stock

object StockDataCache {

    var stockData : MutableMap<String, StockItem> = mutableMapOf()
    var stockPorfolioData : MutableMap<String, Double> = mutableMapOf()

    fun getStocks() : List<StockItem>
    {
        val stockList : MutableList<StockItem> = mutableListOf()
        stockData.forEach{entry ->
            stockList.add(entry.value)
        }
        return stockList
    }

    fun setStocks(stocks : MutableList<StockItem>)
    {
        // clear the old cache and update
        stockData.clear()
        stocks.forEach{
            entry ->
            stockData[entry.symbol] = entry
        }
    }

    fun addStockToCache(stock : StockItem)
    {
        if (checkIfPresentInStockData(stock))
        {
            stockData[stock.symbol] = stock
        }
        else
        {
            stockData[stock.symbol] = stock
        }
    }

    fun addStockToPortFolioCache(stock : String, quantity : Double)
    {
        if(checkIfPresentInStockPortfolioData(stock))
        {
            stockPorfolioData.put(stock, stockPorfolioData.get(stock)!! + quantity)
        }
        else
        {
            stockPorfolioData[stock] = quantity
        }

    }


    private fun checkIfPresentInStockData(queryStock : StockItem) : Boolean
    {
        var result = false
        for (stockName in stockData.keys)
        {
            if (stockName == queryStock.symbol)
            {
                result = true
                break
            }
        }
        return result
    }

    private fun checkIfPresentInStockPortfolioData(queryStock : String) : Boolean
    {
        var result = false
        for (stockName in stockPorfolioData.keys)
        {
            if (stockName == queryStock)
            {
                result = true
                break
            }
        }
        return result
    }

}