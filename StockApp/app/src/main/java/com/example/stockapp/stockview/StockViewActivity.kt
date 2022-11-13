package com.example.stockapp.stockview

import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.stockapp.R
import com.example.stockapp.databinding.ActivityStockViewBinding
import com.example.stockapp.stockservice.StockAPI
import com.example.stockapp.stockservice.StockItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.stockapp.buysellview.BuySellActivity
import com.example.stockapp.home.HomeActivity.Companion.convertToString
import com.example.stockapp.stockcard.StockData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class StockViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockViewBinding
    private lateinit var stockData: StockData

    val SYMBOL : String = "symbol"
    private var stockItem : StockItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockViewBinding.inflate(layoutInflater)
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString("stock_data")
            if (value != null) {
                stockData = Json.decodeFromString(value)
                binding.svStockName.text = stockData.stockName
                binding.svStockPrice.text = convertToString(stockData.stockPrice)
                binding.svStockPriceChange.text = stockData.stockPriceChange
            }
            val sellable = extras.getBoolean("sellable")
            if (!sellable) {
                binding.sellButton.visibility = View.GONE
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val querySymbol : String? = intent.extras?.getString(SYMBOL)
        getStockInformation(querySymbol)


        binding.buyButton.setOnClickListener{
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra("stock_data", Json.encodeToString(stockData))
            intent.putExtra("type", getString(com.example.stockapp.R.string.buy))
            it.context.startActivity(intent)
        }

        binding.sellButton.setOnClickListener{
            val intent = Intent(this, BuySellActivity::class.java)
            intent.putExtra("stock_data", Json.encodeToString(stockData))
            intent.putExtra("type", getString(com.example.stockapp.R.string.sell))
            it.context.startActivity(intent)
        }

        setContentView(binding.root)
    }

    private fun updateUI()
    {
        val stockFragment : Fragment? = supportFragmentManager.findFragmentById(R.id.stock_view_card)
        stockFragment?.view?.findViewById<TextView>(R.id.stock_price)?.text = stockItem?.price.toString()
        stockFragment?.view?.findViewById<TextView>(R.id.stock_price_change)?.text = (stockItem?.changeInPercent?.times(
            100
        )).toString()
    }

    private fun getStockInformation(querySymbol : String?) {
        val stockAPI = StockAPI()
        if (querySymbol != null)
        {
            stockAPI.getStockQuoteValue(querySymbol, object : StockAPI.StockItemListFetched{
                override fun onSuccess(result: StockItem) {
                        stockItem = result
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