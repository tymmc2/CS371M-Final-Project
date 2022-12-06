package com.example.stockapp.totalcard

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.example.stockapp.BaseApplication
import com.example.stockapp.R
import com.example.stockapp.home.HomeActivity.Companion.convertPriceToString
import com.fasterxml.jackson.databind.ser.Serializers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TotalCard : Fragment() {

    companion object {
        fun newInstance() = TotalCard()
    }

    private lateinit var viewModel: TotalCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[TotalCardViewModel::class.java]
        viewModel.init((activity?.application as BaseApplication).database.getDao())

        return inflater.inflate(R.layout.fragment_total_card, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val totalView = view?.findViewById<TextView>(R.id.total)
        val change = view?.findViewById<TextView>(R.id.change)
        viewModel.getPortfolioValue(object : TotalCardViewModel.ComputeListener
        {
            override fun computeFinish(total: Double, priceChange: Double) {
                viewModel.changeMoney(total, priceChange)
                val amount = viewModel.data.amount
                val amtChange = viewModel.data.amtChange
                viewModel.viewModelScope.launch(Dispatchers.Main) {
                    totalView!!.text = convertPriceToString(amount)
                    "${if (amtChange > 0.0) "+" else "" }${convertPriceToString(amtChange)}"
                        .also {
                            change!!.text = it
                            if (amtChange < 0.0) {
                                change.setTextColor(Color.RED)
                            }
                        }
                }
            }

        })



    }

}