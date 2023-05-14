package com.matemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.matemart.databinding.ItemProductAdapterBinding
import com.matemart.model.WishList

class WishLIstAdapter(
    var context: Context,
    var list: List<WishList>,
    var onClick: OnClick
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
        holder.binding.productName.text = list.get(position).p_name
        holder.binding.tvRating.text = list.get(position).average_rating.toString()
        holder.binding.tvTextLeftStock.text =
            "Only " + list.get(position).total_quantity.toString() + " in stock"
        holder.binding.tvOriginalPrice.text = list.get(position).price.toString()
        holder.binding.tvAmount.text = list.get(position).saleprice.toString()
        if (list.get(position).out_of_stock_status == 0) {
            holder.binding.rlBgAlpha.visibility = View.GONE
        } else {
            holder.binding.rlBgAlpha.visibility = View.VISIBLE
        }

        holder.binding.ivRemoveWhishlist.setOnClickListener(){
            onClick.onDelete(list.get(position))
        }


    }


    public interface OnClick {
        public fun onDelete( wishList: WishList);
    }
}