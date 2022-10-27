package com.example.stockapp.stockview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stockapp.databinding.ActivityHomeBinding
import com.example.stockapp.databinding.ActivityStockViewBinding

class StockViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}