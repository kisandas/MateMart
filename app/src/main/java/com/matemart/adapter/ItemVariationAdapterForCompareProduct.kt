package com.matemart.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.matemart.R
import com.matemart.activities.OnCouponSelectedListener
import com.matemart.activities.ProductVariationClickItem
import com.matemart.model.Address
import com.matemart.model.CompareVariation
import com.matemart.model.CouponItemData

class ItemVariationAdapterForCompareProduct(
    private val mContext: Context,
    private val variationList: ArrayList<CompareVariation>?,private val listener: ProductVariationClickItem
) : RecyclerView.Adapter<ItemVariationAdapterForCompareProduct.ItemViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view =
            layoutInflater.inflate(R.layout.item_variation_for_compare_product, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return variationList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = variationList!![position]
        if (position % 2 == 0) {
            holder.parentLayout.setBackgroundColor(mContext.getColor(R.color.gray_eeeeee))

        }else{
            holder.parentLayout.setBackgroundColor(mContext.getColor(R.color.white))
        }

        holder.tvProductName.text = item.value
        holder.tvNetPriceValue.text = "${item.price}"
        holder.tvSalePriceValue.text = "${item.saleprice}"



        holder.tvQTY.text = "${item.currentQty}" // Set quantity from the item object
        val salePriceDouble = item.saleprice?.toDoubleOrNull() ?: 0.0

        holder.tvAddQty.setOnClickListener {
            if (item.currentQty < (item.totalQuantity ?: 0)) {
                item.currentQty++
                holder.tvQTY.text = "${item.currentQty}"

            }
            val Amount :Double = item.currentQty * salePriceDouble
            holder.tvAmount.text =String.format("%.2f", Amount)
            listener.onItemAddQTY(item)
        }

        holder.tvRemoveQTY.setOnClickListener {
            if (item.currentQty > 0) {
                item.currentQty--
                holder.tvQTY.text = "${item.currentQty}"

            }
            val Amount :Double = item.currentQty * salePriceDouble
            holder.tvAmount.text =String.format("%.2f", Amount)
            listener.onItemRemoveQTY(item)
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvProductName: TextView
        val tvNetPriceValue: TextView
        val tvSalePriceValue: TextView
        val tvAddQty: TextView
        val tvQTY: TextView
        val tvRemoveQTY: TextView
        val tvAmount: TextView
        val parentLayout: LinearLayout

        init {
            tvProductName = itemView.findViewById(R.id.tvProductName)
            tvNetPriceValue = itemView.findViewById(R.id.tvNetPriceValue)
            tvSalePriceValue = itemView.findViewById(R.id.tvSalePriceValue)
            tvAddQty = itemView.findViewById(R.id.tvAddQty)
            tvQTY = itemView.findViewById(R.id.tvQTY)
            tvRemoveQTY = itemView.findViewById(R.id.tvRemoveQTY)
            tvAmount = itemView.findViewById(R.id.tvAmount)
            parentLayout = itemView.findViewById(R.id.parentLayout)
        }

    }


}