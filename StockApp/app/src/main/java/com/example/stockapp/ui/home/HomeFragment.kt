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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Load stocks
        val adapter = StockCardAdapter(this.context)
        binding.stocksRecyclerView.adapter = adapter
        val stocksViewModel = ViewModelProvider(this)[StockCardViewModel::class.java]
        stocksViewModel.init()
        binding.stocksRecyclerView.setHasFixedSize(true)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}