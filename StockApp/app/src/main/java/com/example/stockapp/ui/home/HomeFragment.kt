package com.example.stockapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.databinding.FragmentHomeBinding
import com.example.stockapp.stockcard.StockCardAdapter
import com.example.stockapp.stockcard.StockCardViewModel
import com.example.stockapp.stockcard.StockData

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        lateinit var adapter: StockCardAdapter
        lateinit var stocksViewModel: StockCardViewModel

        fun updateData(item: StockData, isAdding: Boolean) {
            if (isAdding) stocksViewModel.addItem(item) else stocksViewModel.removeItem(item)
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
        stocksViewModel.init()
//        binding.stocksRecyclerView.setHasFixedSize(true)

        return root
    }

    override fun onResume() {
        super.onResume()
        if (stocksViewModel.data.isNotEmpty()) {
            binding.emptyHome.visibility = View.GONE
        } else {
            binding.emptyHome.visibility = View.VISIBLE
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}