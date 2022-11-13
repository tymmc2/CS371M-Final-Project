package com.example.stockapp.buysellview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.databinding.ActivityBuySellBinding
import com.example.stockapp.home.HomeActivity.Companion.convertToString
import com.example.stockapp.stockcard.StockCardViewModel
import com.example.stockapp.stockcard.StockData
import com.example.stockapp.ui.home.HomeFragment
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.NumberFormatException
import java.math.BigDecimal

class BuySellActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuySellBinding
    private lateinit var stockData: StockData
    private lateinit var type: String
    private var isSell: Boolean = false
    private var total: Double = 0.0
    private var totalStock: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuySellBinding.inflate(layoutInflater)
        val extras = intent.extras
        if (extras != null) {
            val data = extras.getString("stock_data")
            if (data != null) {
                stockData = Json.decodeFromString(data)
            }
            type = extras.getString("type")!!
            binding.bsButton.text = type
            isSell = type == getString(com.example.stockapp.R.string.sell)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.bsStockFullName.text = stockData.fullStockName
        binding.bsStockName.text = stockData.stockName
        binding.bsSharePrice.text = convertToString(stockData.stockPrice)
        binding.bsEditText.doOnTextChanged { text, _, _, _ ->
            val len = text?.length!!
            binding.bsShareCount.text = if (len in 1..9) text else "0"
            try {
                val value: Int = if (len in 1..9) Integer.parseInt(text.toString()) else 0
                total = value * stockData.stockPrice
                totalStock = value
                binding.bsTotal.text = convertToString(total)
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
            if (isSell) HomeFragment.updateData(stockData, false) else HomeFragment.updateData(stockData, true)
            Toast.makeText(applicationContext,
                "${if (isSell) "Sold" else "Bought"} $totalStock stock of ${stockData.stockName}", Toast.LENGTH_SHORT)
                .show()

        }
        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}