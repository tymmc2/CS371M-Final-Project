package com.example.stockapp.profilecard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockapp.R
import com.example.stockapp.home.HomeActivity.Companion.convertPriceToString

class ProfileCardAdapter(
    private val context: Context?
): RecyclerView.Adapter<ProfileCardAdapter.ProfileCardHolder>() {

    private var items: List<ProfileData> = SampleProfileData.items

    class ProfileCardHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        val profileName: TextView = view!!.findViewById(R.id.profile_name)
        val profileValue: TextView = view!!.findViewById(R.id.profile_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileCardHolder {
        var layout: Int = R.layout.fragment_profile_card

        val inflatedLayout = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)


        return ProfileCardHolder(inflatedLayout)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ProfileCardHolder, position: Int) {
        holder.profileName.text = items[position].profileName
        holder.profileValue.text = context?.getString(R.string.profile_value,
            convertPriceToString(items[position].profileValue))
    }

    fun updateData(list : List<ProfileData>) {
//        items.clear()
//        items.addAll(list)
    }
}