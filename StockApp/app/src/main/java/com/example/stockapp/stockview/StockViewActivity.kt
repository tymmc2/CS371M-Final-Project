package com.example.stockapp.stockview

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import com.example.stockapp.buysellview.BuySellActivity
import com.example.stockapp.databinding.ActivityStockViewBinding
import com.example.stockapp.home.HomeActivity.Companion.convertToString
import com.example.stockapp.stockcard.StockData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class StockViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockViewBinding
    private lateinit var stockData: StockData

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}