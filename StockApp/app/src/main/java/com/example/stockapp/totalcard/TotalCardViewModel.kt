package com.example.stockapp.totalcard

import android.icu.util.CurrencyAmount
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.database.PortfolioDataDao
import com.example.stockapp.stockcard.StockCardViewModel
import com.example.stockapp.stockcard.StockData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal

class TotalCardViewModel : ViewModel() {
    private lateinit var _data : TotalData
    val data : TotalData
        get() = _data
    private lateinit var portfolioDao : PortfolioDataDao

    fun init(dao : PortfolioDataDao) {
        _data = TotalData(0.0, 0.0)
        portfolioDao = dao
    }

    fun getPortfolioValue(callback : ComputeListener)
    {
        viewModelScope.launch(Dispatchers.IO) {
            val portfolioStocks = portfolioDao.getAllSync()
            var portfolio: MutableMap<String, StockData> = mutableMapOf()
            var stockList: MutableList<StockData> = mutableListOf()
            for (stock in portfolioStocks) {
                val sym = stock.symbol.toString()
                if (portfolio.contains(sym)) {
                    if (stock.trade == "B") {
                        portfolio[sym]?.quantityHolding =
                            portfolio[sym]?.quantityHolding?.plus(stock.holding)!!
                    } else {
                        portfolio[sym]?.quantityHolding =
                            portfolio[sym]?.quantityHolding?.minus(stock.holding)!!
                    }
                } else {
                    portfolio[sym] = StockData(
                        stock.symbol.toString(),
                        stock.name.toString(),
                        stock.price.toDouble(),
                        stock.change,
                        stock.holding.toDouble()
                    )
                }
            }
            var total: Double = 0.0
            for (value in portfolio.values.toList())
                total += value.quantityHolding * value.stockPrice

            var priceChange = 0.0
            if (portfolioStocks.isNotEmpty()) {
                val lastItem = portfolioStocks[portfolioStocks.size - 1]
                val diff = lastItem.price * lastItem.holding
                priceChange = if (lastItem.trade == "B") diff else -diff
            }
            callback.computeFinish(total, priceChange)
        }
    }

    fun changeMoney(amount: Double, change: Double) {
        data.amount += amount
        data.amtChange = change
    }

    interface ComputeListener
    {
        fun computeFinish(total : Double, priceChange : Double)
    }
}