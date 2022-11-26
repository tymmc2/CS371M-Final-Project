package com.example.stockapp.buysellview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.BaseApplication
import com.example.stockapp.databinding.ActivityBuySellBinding
import com.example.stockapp.home.HomeActivity
import com.example.stockapp.home.HomeActivity.Companion.convertPriceToString
import com.example.stockapp.stockcard.StockData
import com.example.stockapp.stockview.StockViewActivity
import com.example.stockapp.ui.home.HomeFragment
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.NumberFormatException


class BuySellActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuySellBinding
    private lateinit var stockData: StockData
    private lateinit var type: String
    private var isSell: Boolean = false
    private var total: Double = 0.0
    private var totalStock: Int = 0

    private lateinit var stocksViewModel : BuySellViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuySellBinding.inflate(layoutInflater)

        stocksViewModel = ViewModelProvider(this)[BuySellViewModel::class.java]
        stocksViewModel.init((application as BaseApplication).database.getDao())

        val extras = intent.extras
        if (extras != null) {
            val data = extras.getString(StockViewActivity.STOCK_DATA)
            if (data != null) {
                stockData = Json.decodeFromString(data)
            }
            type = extras.getString(StockViewActivity.TYPE)!!
            binding.bsButton.text = type
            isSell = type == getString(com.example.stockapp.R.string.sell)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.bsStockFullName.text = stockData.fullStockName
        binding.bsStockName.text = stockData.stockName
        binding.bsSharePrice.text = convertPriceToString(stockData.stockPrice)
        binding.bsEditText.doOnTextChanged { text, _, _, _ ->
            val len = text?.length!!
            binding.bsShareCount.text = if (len in 1..9) text else "0"
            try {
                val value: Int = if (len in 1..9) Integer.parseInt(text.toString()) else 0
                total = value * stockData.stockPrice
                totalStock = value
                binding.bsTotal.text = convertPriceToString(total)
            } catch (e: NumberFormatException) {
                total = 0.0
                totalStock = 0
                binding.bsTotal.text = "0"
            }
        }
        binding.bsEditText.setOnEditorActionListener { view, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEND) {
                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
                return@setOnEditorActionListener true
            }
            false
        }
        binding.bsButton.setOnClickListener {

            stockData.quantityHolding = binding.bsEditText.text.toString().toDouble()
            if (isSell) {
                HomeFragment.updateData(stockData, false, applicationContext)
                stocksViewModel.updateStock(stockData, "S")
            } else {
                HomeFragment.updateData(stockData, true,applicationContext)
                stocksViewModel.insertStock(stockData, "B")
            }

            Toast.makeText(applicationContext,
                "${if (isSell) "Sold" else "Bought"} $totalStock stock of ${stockData.stockName}", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }

        binding.gotoHomeBtn.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}