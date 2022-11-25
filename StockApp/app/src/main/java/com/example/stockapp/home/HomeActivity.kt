package com.example.stockapp.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.stockapp.R
import com.example.stockapp.databinding.ActivityHomeBinding
import com.example.stockapp.search.AllStockCache
import com.example.stockapp.search.SearchActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat

class HomeActivity : AppCompatActivity() {
    companion object {
        fun convertPriceToString(value: Double) : String {
            val numberFormat = NumberFormat.getCurrencyInstance()
            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 2

            return numberFormat.format(value)
        }

        fun convertPriceChangeToString(value: Double) : String {
            val numberFormat = NumberFormat.getPercentInstance()
            numberFormat.maximumFractionDigits = 2
            numberFormat.minimumFractionDigits = 2

            return "${if (value > 0.0) "+" else "-"}${numberFormat.format(value)}"
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

        AllStockCache.getAllStockCache()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.searchFab.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            it.context.startActivity(intent)
        }
    }
}