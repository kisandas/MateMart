package com.matemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.matemart.databinding.ItemProductAdapterBinding

class WishLIstAdapter(
    var context: Context,
    var list: List<String>
) : RecyclerView.Adapter<WishLIstAdapter.MyViewHolder>() {


    class MyViewHolder(var binding: ItemProductAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemProductAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.ivMinus.visibility = View.GONE
        holder.binding.tvCount.visibility = View.GONE

    }
}