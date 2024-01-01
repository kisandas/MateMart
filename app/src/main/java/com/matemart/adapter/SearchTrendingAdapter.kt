package com.matemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.matemart.model.RecentlySearch
import com.matemart.R
import com.matemart.model.Address
import com.matemart.model.TrendingSearch
import com.matemart.activities.ItemClickListener

class SearchTrendingAdapter(
    private val mContext: Context,
    private val itemList: ArrayList<String>,
    private val listener: ItemClickListener,
) : RecyclerView.Adapter<SearchTrendingAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_trending_search_adapter, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size ?: 0
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.tvProductName.text = item

        holder.itemView.setOnClickListener {
            listener.onSearchItemClick(item)
        }


    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView


        init {
            tvProductName = itemView.findViewById(R.id.tvProductName)
        }


    }


}