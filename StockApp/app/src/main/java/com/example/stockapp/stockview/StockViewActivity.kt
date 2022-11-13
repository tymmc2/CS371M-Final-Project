package com.example.stockapp.stockview

import android.os.Bundle
import android.view.MenuItem
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


class StockViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockViewBinding

    val SYMBOL : String = "symbol"
    private var stockItem : StockItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockViewBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        val querySymbol : String? = intent.extras?.getString(SYMBOL)
        getStockInformation(querySymbol)


        binding.buyButton.setOnClickListener{
            Toast.makeText(applicationContext, "Bought stock!", Toast.LENGTH_SHORT).show()
        }

        binding.sellButton.setOnClickListener{
            Toast.makeText(applicationContext, "Sold stock!", Toast.LENGTH_SHORT).show()
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