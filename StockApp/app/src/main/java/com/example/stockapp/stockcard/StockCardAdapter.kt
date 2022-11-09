package com.example.stockapp.stockcard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockapp.R
import com.example.stockapp.home.HomeActivity.Companion.convertToString
import com.example.stockapp.stockview.StockViewActivity
import com.example.stockapp.ui.home.HomeFragment
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class StockCardAdapter(
    private val context: Context?,
    private val sellable: Boolean
): RecyclerView.Adapter<StockCardAdapter.StockCardHolder>() {

    private var items: ArrayList<StockData> = if (!sellable) SampleData.items else ArrayList()

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
        println("Updating data with list length of ${list.size}")
        items.clear()
        items.addAll(list)
        println("new length = ${items.size}")
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (items.size > 0) items.size else 0
    }

    override fun onBindViewHolder(holder: StockCardHolder, position: Int) {
        // Update the stock card here
        val stockCard: CardView = holder.stockCard
        stockCard.setOnClickListener{
            val intent = Intent(this@StockCardAdapter.context, StockViewActivity::class.java)
            intent.putExtra("stock_data", Json.encodeToString(items[position]))
            intent.putExtra("sellable", sellable);
            it.context.startActivity(intent)
        }
        holder.stockName.text = items[position].stockName
        holder.stockPrice.text = convertToString(items[position].stockPrice)
        holder.stockPriceChange.text = items[position].stockPriceChange
    }
}
