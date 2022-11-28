package com.example.stockapp.stockcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.database.PortfolioDataDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StockCardViewModel : ViewModel() {
    private lateinit var _data : ArrayList<StockData>
    val data : List<StockData>
    get() = _data
    private lateinit var portfolioDao : PortfolioDataDao

    fun init(dao: PortfolioDataDao) {
        _data = ArrayList()
        portfolioDao = dao
    }

    fun getAllPortfolio(callback : DatabaseFetchListener)
    {
        viewModelScope.launch(Dispatchers.IO) {
            val portfolioStocks = portfolioDao.getAllSync()
            var portfolio : MutableMap<String, StockData> = mutableMapOf()
            var stockList : MutableList<StockData> = mutableListOf()
            for( stock in portfolioStocks)
            {
                val sym = stock.symbol.toString()
                if (portfolio.contains(sym))
                {
                    if (stock.trade == "B")
                    {
                        portfolio[sym]?.quantityHolding = portfolio[sym]?.quantityHolding?.plus(stock.holding)!!
                    } 
                    else
                    {
                        portfolio[sym]?.quantityHolding = portfolio[sym]?.quantityHolding?.minus(stock.holding)!!
                    }
                }
                else
                {
                    portfolio[sym] = StockData(stock.symbol.toString(), stock.name.toString(), stock.price.toDouble() ,stock.change, stock.holding.toDouble())
                }
            }
            callback.onSuccess(portfolio.values.toList())
        }

    }
    fun addList(list: List<StockData>) {
        _data.clear()
        _data.addAll(list)
    }

    fun addItem(item: StockData) {
        _data.add(item)
    }

    fun removeItem(item: StockData) {
        _data.remove(item)
    }

    interface DatabaseFetchListener
    {
        fun onSuccess(stockList : List<StockData>)
    }
}