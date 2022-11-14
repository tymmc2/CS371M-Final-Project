package com.example.stockapp.totalcard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.stockapp.R
import com.example.stockapp.home.HomeActivity.Companion.convertPriceToString

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
        viewModel.init()

        return inflater.inflate(R.layout.fragment_total_card, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val total = view?.findViewById<TextView>(R.id.total)
        val change = view?.findViewById<TextView>(R.id.change)
        viewModel.changeMoney(500.0)

        val amount = viewModel.data.amount
        val amtChange = viewModel.data.amtChange

        total!!.text = convertPriceToString(amount)
        "${if (amtChange > 0.0) "+" else "" }${convertPriceToString(amtChange)}"
            .also { change!!.text = it }
    }

}