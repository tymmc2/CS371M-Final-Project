package com.example.stockapp.stockcard

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import com.example.stockapp.databinding.FragmentStockCardBinding
import com.example.stockapp.stockview.StockViewActivity

class StockCard : Fragment() {

    companion object {
        fun newInstance() = StockCard()
    }

    private lateinit var viewModel: StockCardViewModel
    private var _binding: FragmentStockCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStockCardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[StockCardViewModel::class.java]
        // TODO: Use the ViewModel
    }
}