package com.example.stockapp.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.stockcard.StockCardAdapter
import com.example.stockapp.databinding.ActivityHomeBinding
import com.example.stockapp.stockcard.StockCardViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load stocks
        val adapter = StockCardAdapter(applicationContext)
        binding.stocksRecyclerView.adapter = adapter
        val stocksViewModel = ViewModelProvider(this)[StockCardViewModel::class.java]
        stocksViewModel.init()
        binding.stocksRecyclerView.setHasFixedSize(true)
    }
}