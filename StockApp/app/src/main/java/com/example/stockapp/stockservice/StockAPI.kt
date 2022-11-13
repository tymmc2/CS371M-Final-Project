package com.example.stockapp.stockservice

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import yahoofinance.Stock
import yahoofinance.YahooFinance
import java.lang.Exception
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class StockAPI {

    private fun buildStockItem(stock : Stock): StockItem {
        return StockItem(
            stock.name,
            stock.currency,
            stock.symbol,
            stock.stockExchange,
            stock.quote.price.toDouble(),
            stock.quote.avgVolume.toDouble(),
            stock.quote.volume.toDouble(),
            stock.quote.dayHigh.toDouble(),
            stock.quote.dayLow.toDouble(),
            stock.quote.yearHigh.toDouble(),
            stock.quote.yearLow.toDouble(),
            stock.quote.open.toDouble(),
            stock.quote.previousClose.toDouble(),
            stock.stats.marketCap.toDouble(),
            stock.stats.eps.toDouble(),
            stock.quote.change.toDouble(),
            stock.quote.changeInPercent.toDouble(),
            stock.dividend.annualYield.toDouble(),
            stock.dividend.annualYieldPercent.toDouble()
        )
    }

    private fun buildStockItemFromResponse(result: JSONObject) : StockItem {
        return StockItem(
            result.getString("displayName"),
            result.getString("currency"),
            result.getString("symbol"),
            result.getString("exchange"),
            result.getDouble("regularMarketPrice"),
            result.getDouble("averageDailyVolume10Day"),
            result.getDouble("averageDailyVolume3Month"),
            result.getDouble("regularMarketDayHigh"),
            result.getDouble("regularMarketDayLow"),
            result.getDouble("fiftyTwoWeekHigh"),
            result.getDouble("fiftyTwoWeekLow"),
            result.getDouble("regularMarketOpen"),
            result.getDouble("regularMarketPreviousClose"),
            result.getDouble("marketCap"),
            result.getDouble("epsCurrentYear"),
            result.getDouble("fiftyDayAverageChange"),
            result.getDouble("fiftyDayAverageChangePercent"),
            result.getDouble("trailingAnnualDividendYield"),
            result.getDouble("trailingAnnualDividendRate")
        )
    }

    private fun getStockQuote(symbol : String): StockItem? {

        var result: StockItem? = null
        try {
            Log.d("Stock API", "network call")
            val stock: Stock? = YahooFinance.get(symbol)
            result = stock?.let { buildStockItem(it) }
        } catch (exception: Exception) {
            Log.d("Stock API", "network call failed for stock : " + exception.message)
        }
        return result
    }

    private fun getStockQuotesPortfolio(symbols : Array<String>): MutableList<StockItem> {
        var stockItemList : MutableList<StockItem> = mutableListOf()
        try {
            //TODO add a broadcast
            val stockMap: MutableMap<String, Stock>? = YahooFinance.get(symbols)

            for (symbol in symbols)
            {
                val stock = stockMap?.get(symbol)
                val stockItem = stock?.let { buildStockItem(it) }
                stockItem?.let { stockItemList.add(it) }
            }

        } catch (exception: Exception) {
            Log.d("Stock API", "network batch call failed for stock : " + exception.message)
        }
        return stockItemList
    }

    fun getStockQuoteValue(name : String, callback : StockItemListFetched)
    {
        GlobalScope.launch (Dispatchers.IO){
            val job = async { YahooFinanceQuoteProvider().getStockQuote(name) }
            val result = job.await()
            val stockResult = result?.let { buildStockItemFromResponse(it) }

            if (stockResult != null) {
                callback.onSuccess(stockResult)
            }
            else
                callback.onError()
        }
    }

    public interface StockItemListFetched
    {
        fun onSuccess(result : StockItem)
        fun onError()
    }

}