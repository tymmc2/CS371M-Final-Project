package com.example.stockapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.BaseApplication
import com.example.stockapp.databinding.FragmentHomeBinding
import com.example.stockapp.stockcache.StockDataCache
import com.example.stockapp.stockcache.StockSharedPref
import com.example.stockapp.stockcard.StockCardAdapter
import com.example.stockapp.stockcard.StockCardViewModel
import com.example.stockapp.stockcard.StockData
import com.example.stockapp.stockservice.StockAPI
import com.example.stockapp.stockservice.StockItem
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        lateinit var adapter: StockCardAdapter
        lateinit var stocksViewModel: StockCardViewModel

        fun updateData(item: StockData, isAdding: Boolean, context: Context) {
            if (isAdding) stocksViewModel.addItem(item) else stocksViewModel.removeItem(item)
            StockDataCache.addStockToPortFolioCache(item.stockName, item.quantityHolding)
            StockSharedPref(context).storePortfolioStockData(StockDataCache.stockPorfolioData)
            adapter.updateData(stocksViewModel.data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Load stocks
        adapter = StockCardAdapter(this.context, true)
        binding.stocksRecyclerView.adapter = adapter
        stocksViewModel = ViewModelProvider(this)[StockCardViewModel::class.java]
        stocksViewModel.init((activity?.application as  BaseApplication).database.getDao())

        stocksViewModel.getAllPortfolio(object : StockCardViewModel.DatabaseFetchListener
        {
            override fun onSuccess(stockList: List<StockData>) {
                // Hide the banner
                adapter.updateData(stockList)
                updateEmptyHomeText()
            }

        })
        updateEmptyHomeText()

        return root
    }

    private fun updateEmptyHomeText() {
        if (adapter.itemCount > 0) {
            binding.emptyHome.visibility = View.GONE
        } else {
            binding.emptyHome.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        updateEmptyHomeText()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}