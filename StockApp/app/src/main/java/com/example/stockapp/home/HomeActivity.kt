package com.example.stockapp.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.stockapp.R
import com.example.stockapp.buysellview.BuySellActivity
import com.example.stockapp.stockcard.StockCardAdapter
import com.example.stockapp.databinding.ActivityHomeBinding
import com.example.stockapp.search.SearchActivity
import com.example.stockapp.stockcard.StockCardViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import java.text.NumberFormat

class HomeActivity : AppCompatActivity() {
    companion object {
        fun convertToString(value: Double) : String {
            val numberFormat = NumberFormat.getCurrencyInstance()
            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 2

            return numberFormat.format(value)
        }
    }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.searchFab.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            it.context.startActivity(intent)
        }
    }
}