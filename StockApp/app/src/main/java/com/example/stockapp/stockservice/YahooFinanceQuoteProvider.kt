package com.example.stockapp.stockservice
import com.example.stockapp.stockservice.StockAPI.Companion.inputStreamToString
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection


class YahooFinanceQuoteProvider{
    private val baseUrl = "https://query1.finance.yahoo.com/v7/finance/quote?symbols="
    private val userAgent = "Mozilla/5.0 (Linux; Android) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Mobile Safari/537.36"

    fun getStockQuote(symbol: String): JSONObject? {
        val endpoint = URL(baseUrl + URLEncoder.encode(symbol, "utf-8"))
        val connection: HttpsURLConnection = endpoint.openConnection() as HttpsURLConnection
        connection.setRequestProperty("User-Agent", userAgent);

        if (connection.responseCode == 200) {
            connection.inputStream.use { inputStream ->
                val jsonString = inputStreamToString(inputStream)
                val rootObject = JSONObject(jsonString)
                val quoteResponseObject = rootObject.getJSONObject("quoteResponse")
                val resultObject = quoteResponseObject.getJSONArray("result").getJSONObject(0)
                return resultObject
            }

        } else
            throw Exception("Error: YahooFinanceQuoteProvider response code " + connection.responseCode)
    }
}