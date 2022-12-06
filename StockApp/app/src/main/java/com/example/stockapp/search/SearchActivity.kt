package com.example.stockapp.search

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.databinding.ActivitySearchBinding
import com.example.stockapp.stockcard.Stock
import com.example.stockapp.stockcard.StockData
import com.example.stockapp.stockservice.StockAPI
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val CSV_FILE: String = "src/resources/all_stocks.csv"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = SearchViewAdapter(this, false)
        binding.stocksRecyclerView.adapter = adapter
        val stocksViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
//        loadStocksFromCsv()
        stocksViewModel.init()
        binding.stocksRecyclerView.setHasFixedSize(true)

        binding.searchBar.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                StockAPI().searchStocks(binding.searchBar.text.toString(), object : StockAPI.StockSearchFetched {
                    override fun onSuccess(result: List<StockData>) {
                        GlobalScope.launch(Dispatchers.Main){
                            if (result.isEmpty()) {
                                Toast.makeText(applicationContext, "No stocks found", Toast.LENGTH_LONG).show()
                            }
                            stocksViewModel.addList(result)
                            adapter.updateData(result)
                            adapter.notifyDataSetChanged()
                            val imm: InputMethodManager =
                                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)

                        }
                    }

                    override fun onError() {
                        println("Failed to search for stocks")
                        GlobalScope.launch(Dispatchers.Main) {
                            Toast.makeText(applicationContext, "Failed to search for stocks", Toast.LENGTH_LONG).show()
                        }
                    }
                })
                return@OnEditorActionListener true
            }
            false
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    fun loadStocksFromCsv() {
        val stream = File(CSV_FILE).inputStream();
        val list = csvReader().open(stream) {
            readAllWithHeaderAsSequence().map {
                Stock(
                    it["Symbol"]!!,
                    it["Name"]!!,
                    it["Last Sale"]!!
                )
            }.toList()
        }
        println("${list.size} ${list[0].Symbol}")
    }
}