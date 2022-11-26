package com.example.stockapp

import android.app.Application
import com.example.stockapp.database.PortfolioDatabase

class BaseApplication : Application() {

    val database: PortfolioDatabase by lazy {
        PortfolioDatabase.getDatabase(this)
    }
}