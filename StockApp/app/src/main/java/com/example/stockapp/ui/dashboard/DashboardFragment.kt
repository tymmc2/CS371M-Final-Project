package com.example.stockapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.stockapp.BaseApplication
import com.example.stockapp.R
import com.example.stockapp.database.PortfolioDataDao
import com.example.stockapp.databinding.FragmentDashboardBinding
import com.example.stockapp.stockcard.StockData
import com.example.stockapp.stockview.LineGraphFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var portfolioDao: PortfolioDataDao

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        portfolioDao = (activity?.application as BaseApplication).database.getDao()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    fun updateUI() {
        getPortfolioHistory(object : PortfolioHistoryCallback {
            override fun computeFinish(list: List<Double>, priceChange: Double) {
                GlobalScope.launch(Dispatchers.Main) {

                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.db_line_graph, LineGraphFragment.newInstance(list, priceChange))
                    transaction.commit()
                }
            }

        })
    }

    private fun getPortfolioHistory(callback: PortfolioHistoryCallback) {
        GlobalScope.launch(Dispatchers.IO) {
            val portfolioStocks = portfolioDao.getAllSync()
            var portfolio: MutableMap<String, StockData> = mutableMapOf()
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
            var list: ArrayList<Double> = ArrayList()
            list.add(total)
            for (i in portfolioStocks.size - 1 downTo portfolioStocks.size - 13) {
                if (i >= 0) {
                    val currentItem = portfolioStocks[i]
                    val diff = currentItem.price * currentItem.holding
                    list.add(0, list[0] + if (currentItem.trade == "B") -diff else diff)
                }
            }
            callback.computeFinish(list, list[list.size - 1] - list[list.size - 2])
        }
    }

    interface PortfolioHistoryCallback {
        fun computeFinish(list: List<Double>, priceChange: Double)
    }
}