package com.example.stockapp.stockcard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stockapp.R

class StockCardAdapter(
    private val context: Context?
): RecyclerView.Adapter<StockCardAdapter.StockCardHolder>() {

    private var items: ArrayList<StockData> = ArrayList()

    class StockCardHolder(view: View?): RecyclerView.ViewHolder(view!!) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockCardHolder {
        var layout: Int = R.layout.fragment_stock_card

        val inflatedLayout = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        return StockCardHolder(inflatedLayout)
    }

    fun updateData(list: List<StockData>) {
        items.clear()
        items.addAll(list)
    }

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: StockCardHolder, position: Int) {
        // Update the stock card here
    }
}
