package com.example.stockapp.stockview

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stockapp.databinding.ActivityStockViewBinding
import com.example.stockapp.home.HomeActivity


class StockViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStockViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockViewBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        binding.buyButton.setOnClickListener{
            Toast.makeText(applicationContext, "Bought stock!", Toast.LENGTH_SHORT).show()
        }

        binding.sellButton.setOnClickListener{
            Toast.makeText(applicationContext, "Sold stock!", Toast.LENGTH_SHORT).show()
        }

        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}