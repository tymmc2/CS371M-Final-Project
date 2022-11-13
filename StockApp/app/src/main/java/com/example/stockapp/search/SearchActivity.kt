package com.example.stockapp.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.R
import com.example.stockapp.databinding.ActivityHomeBinding
import com.example.stockapp.databinding.ActivitySearchBinding
import com.example.stockapp.databinding.FragmentProfileBinding
import com.example.stockapp.stockcard.StockCardAdapter
import com.example.stockapp.stockcard.StockCardViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = StockCardAdapter(this, false)
        binding.stocksRecyclerView.adapter = adapter
        val stocksViewModel = ViewModelProvider(this)[StockCardViewModel::class.java]
        stocksViewModel.init()
        binding.stocksRecyclerView.setHasFixedSize(true)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}