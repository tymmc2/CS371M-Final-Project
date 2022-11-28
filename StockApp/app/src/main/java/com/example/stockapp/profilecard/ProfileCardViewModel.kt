package com.example.stockapp.profilecard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.database.PortfolioDataDao
import com.example.stockapp.stockcard.StockData
import com.example.stockapp.totalcard.TotalCardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileCardViewModel : ViewModel() {
    private lateinit var _data : MutableList<ProfileData>;
    val data : List<ProfileData>
    get() = _data;
    private lateinit var portfolioDao : PortfolioDataDao

    fun init(dao : PortfolioDataDao) {
        _data = mutableListOf()
        portfolioDao = dao
    }

    fun add(data : ProfileData) {
        _data.add(data)
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
            callback.computeFinish(total)
        }
    }

    interface ComputeListener
    {
        fun computeFinish(total : Double)
    }
}