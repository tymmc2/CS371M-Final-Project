package com.example.stockapp.stockservice

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.stockapp.search.AllStockCache
import com.example.stockapp.stockcard.Stock
import com.example.stockapp.stockcard.StockData
import com.example.stockapp.stockservice.StockAPI.Companion.inputStreamToString
import com.github.doyaaaaaken.kotlincsv.client.CsvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.net.URL
import java.net.URLEncoder
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class YahooFinanceSearch {
    private val SEARCH_URL = "https://query1.finance.yahoo.com/v1/finance/search?q="
    private val ALL_STOCKS_URL = "https://api.nasdaq.com/api/screener/stocks?tableonly=true&limit=25&offset=0&download=true"
    private val USER_AGENT = "Mozilla/5.0 (Linux; Android) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Mobile Safari/537.36"

    fun search(symbol: String): List<StockData> {
        val endpoint = URL(SEARCH_URL + URLEncoder.encode(symbol, "utf-8"))
        val connection: HttpsURLConnection = endpoint.openConnection() as HttpsURLConnection
        connection.setRequestProperty("User-Agent", USER_AGENT)

        if (connection.responseCode == 200) {
            connection.inputStream.use { inputStream ->
                val jsonString = inputStreamToString(inputStream)
                val rootObject = JSONObject(jsonString)
                return convertJsonToStocksYahoo(rootObject.getJSONArray("quotes"))
            }
        } else {
            throw Exception("Error: YahooFinanceSearch response code " + connection.responseCode)
        }
    }

    fun fetchAllStocks(): List<StockData> {
        val endpoint = URL(ALL_STOCKS_URL)
        val connection: HttpsURLConnection = endpoint.openConnection() as HttpsURLConnection
        connection.setRequestProperty("User-Agent", USER_AGENT)

        if (connection.responseCode == 200) {
            connection.inputStream.use { inputStream ->
                val jsonString = inputStreamToString(inputStream)
                val rootObject = JSONObject(jsonString)
                val resultObject = rootObject.getJSONObject("data")
                return convertJsonToStocksNasdaq(resultObject.getJSONArray("rows"))
            }
        } else {
            throw Exception("Error: YahooFinanceSearch response code " + connection.responseCode)
        }
    }

    private fun convertJsonToStocksNasdaq(arr: JSONArray): List<StockData> {
        val newList: ArrayList<StockData> = ArrayList()
        for (i in 0 until arr.length()) {
            val currItem = arr.getJSONObject(i)
            val symbol = currItem.getString("symbol")
            newList.add(StockData(
                currItem.getString("symbol"),
                currItem.getString("name"),
                currItem.getString("lastsale").split("$")[1].toDouble() ?: 0.0,
                currItem.getString("pctchange") ?: "0.00%",
                0.0
            ))
        }
        return newList
    }

    private fun convertJsonToStocksYahoo(arr: JSONArray): List<StockData> {
        val newList: ArrayList<StockData> = ArrayList()
        for (i in 0 until arr.length()) {
            val currItem = arr.getJSONObject(i)
            val symbol = currItem.getString("symbol")
            val stock = AllStockCache.getAllStockCache().getStock(symbol)
            if (stock == null) {
                continue;
            }
            newList.add(StockData(
                currItem.getString("symbol"),
                currItem.getString("shortname"),
                stock.stockPrice,
                stock.stockPriceChange,
                0.0
            ))
        }
        return newList
    }
}