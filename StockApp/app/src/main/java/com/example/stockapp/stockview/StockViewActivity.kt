package com.example.stockapp.stockview

import androidx.appcompat.app.AppCompatActivity
import com.example.stockapp.databinding.ActivityStockViewBinding
import com.example.stockapp.stockservice.StockAPI
import com.example.stockapp.stockservice.StockItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.stockapp.R
import com.example.stockapp.buysellview.BuySellActivity
import com.example.stockapp.stockcache.StockDataCache
import com.example.stockapp.stockcache.StockSharedPref
import com.example.stockapp.home.HomeActivity.Companion.convertPriceChangeToString
import com.example.stockapp.home.HomeActivity.Companion.convertPriceToString
import com.example.stockapp.stockcard.StockData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class StockViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockViewBinding
    private lateinit var stockData: StockData

    companion object {
        const val SYMBOL : String = "symbol"
        const val PRICE_CHANGE : String = "price_change"
        const val STOCK_DATA : String = "stock_data"
        const val SELLABLE : String = "sellable"
        const val TYPE : String = "type"
        const val CURRENT_PRICE : String = "current_price"
        const val HISTORY : String = "stock_history"
    }
    private var stockItem : StockItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockViewBinding.inflate(layoutInflater)
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString(STOCK_DATA)
            if (value != null) {
                stockData = Json.decodeFromString(value)
            }
            val sellable = extras.getBoolean(SELLABLE)
            if (!sellable) {
                binding.sellButton.visibility = View.GONE
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val querySymbol : String? = intent.extras?.getString(SYMBOL)
        getStockInformation(querySymbol)


        binding.buyButton.setOnClickListener{
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra(STOCK_DATA, Json.encodeToString(stockData))
            intent.putExtra(TYPE, getString(com.example.stockapp.R.string.buy))
            it.context.startActivity(intent)
        }

        binding.sellButton.setOnClickListener{
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra(STOCK_DATA, Json.encodeToString(stockData))
            intent.putExtra(TYPE, getString(com.example.stockapp.R.string.sell))
            it.context.startActivity(intent)
        }

        setContentView(binding.root)
    }

    private fun updateUI()
    {
        if (stockItem != null) {
            binding.svStockName.text= stockItem?.name.toString()
            binding.svStockPrice.text = stockItem?.price?.let { convertPriceToString(it) }
            stockData.stockPrice = stockItem?.price!!
            val changeStr = stockItem?.changeInPercent?.let {
                val str = convertPriceChangeToString(it)
                if (it < 0.0) binding.svStockPriceChange.setTextColor(Color.RED)
                str
            }
            binding.svStockPriceChange.text = changeStr
            stockData.stockPriceChange = changeStr!!

            binding.svHigh.text = stockItem?.dayHigh?.toString()
            binding.svLow.text = stockItem?.dayLow?.toString()
            binding.svOpen.text = stockItem?.open?.toString()
            binding.svPreviousClose.text = stockItem?.previousClose?.toString()
            binding.svVolume.text = stockItem?.volume?.toString()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.sv_line_graph, LineGraphFragment.newInstance(stockItem!!.symbol, stockItem?.changeInPercent, stockItem?.price!!.toFloat()))
            transaction.commit()
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getStockInformation(querySymbol : String?) {
        val stockAPI = StockAPI()
        if (querySymbol != null)
        {
            stockAPI.getStockQuoteValue(querySymbol, object : StockAPI.StockItemListFetched{
                override fun onSuccess(result: StockItem) {
                        stockItem = result
                        StockDataCache.addStockToCache(stockItem!!)
                        GlobalScope.launch(Dispatchers.Main) { updateUI() }
                }

                override fun onError() {

                }

            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}