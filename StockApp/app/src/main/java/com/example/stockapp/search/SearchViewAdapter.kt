package com.example.stockapp.search

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockapp.R
import com.example.stockapp.home.HomeActivity.Companion.convertPriceToString
import com.example.stockapp.stockcard.SampleData
import com.example.stockapp.stockcard.StockCardAdapter
import com.example.stockapp.stockcard.StockData
import com.example.stockapp.stockview.StockViewActivity
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import yahoofinance.Stock

class SearchViewAdapter(
    private val context: Context?,
    private val sellable: Boolean
): RecyclerView.Adapter<SearchViewAdapter.StockCardHolder>() {

    private var items: ArrayList<StockData> = ArrayList()

    class StockCardHolder(view: View?): RecyclerView.ViewHolder(view!!) {
        val stockCard: CardView = view!!.findViewById(R.id.stock_card)
        val stockName: TextView = view!!.findViewById(R.id.stock_name)
        val stockPrice: TextView = view!!.findViewById(R.id.stock_price)
        val stockPriceChange: TextView = view!!.findViewById(R.id.stock_price_change)
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
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (items.size > 0) items.size else 0
    }

    override fun onBindViewHolder(holder: StockCardHolder, position: Int) {
        // Update the stock card here
        val stockCard: CardView = holder.stockCard
        stockCard.setOnClickListener{

            val intent = Intent(this@SearchViewAdapter.context, StockViewActivity::class.java)
            val stockSymbol = stockCard.findViewById<TextView>(R.id.stock_name).text
            intent.putExtra(StockViewActivity.SYMBOL, stockSymbol)
            intent.putExtra(StockViewActivity.STOCK_DATA, Json.encodeToString(items[position]))
            intent.putExtra(StockViewActivity.SELLABLE, sellable);
            it.context.startActivity(intent)
        }
        holder.stockName.text = items[position].stockName
        holder.stockPrice.text = convertPriceToString(items[position].stockPrice)
        holder.stockPriceChange.text = items[position].stockPriceChange
        if (items[position].stockPriceChange.contains("-")) {
            holder.stockPriceChange.setTextColor(Color.RED)
        }
    }
}
