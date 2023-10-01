package com.matemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.matemart.R
import com.matemart.activities.OnCouponSelectedListener
import com.matemart.model.CouponItemData

class CouponItemAdapter(
    private val amount: Double,
    private val viewList: ArrayList<CouponItemData>?,
    private val mContext: Context,

    private val listener: OnCouponSelectedListener,
) : RecyclerView.Adapter<CouponItemAdapter.ItemViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_coupon_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return viewList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = viewList!![position]

        holder.tvCouponCode.text = item.code

        holder.tvCouponDescription.text = item.description
        if (!item.eDate.isNullOrEmpty()) {
            holder.tvCouponExpiry.visibility = VISIBLE
            holder.tvCouponExpiry.text = "Expier On: ${item.eDate}"
        } else {
            holder.tvCouponExpiry.visibility = GONE
        }
        if (item.type == 0) {

            holder.tvSaveAmount.text = "Save ₹${item.amount}"
        } else if (item.type == 1) {

            val dicountAmount = (amount * item.amount!!.toInt()) / 100

            holder.tvSaveAmount.text = "Save ₹$dicountAmount"
        }

        // Set the checkbox state based on the selectedPosition
        holder.icCheckBox.isChecked = position == selectedPosition

        holder.itemView.setOnClickListener {
            // Update the selected position and refresh the list
            if (position == selectedPosition) {
                // If the clicked item is already selected, unselect it
                selectedPosition = -1 // No item is selected
                holder.tvCouponCode.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.dark_gray_4d4d4d
                    )
                )
            } else {
                // Update the selected position
                selectedPosition = position
                holder.tvCouponCode.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.theme_blue_38B449
                    )
                )
            }

            // Refresh the list to reflect the changes
            notifyDataSetChanged()

            // Notify the listener with the selected item (or null if nothing is selected)
            val selectedItem = if (selectedPosition != -1) viewList[selectedPosition] else null
            listener.onCouponSelected(selectedItem)
        }

        holder.icCheckBox.setOnClickListener {
            // Update the selected position and refresh the list
            if (position == selectedPosition) {
                // If the clicked item is already selected, unselect it
                selectedPosition = -1 // No item is selected
                holder.tvCouponCode.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.dark_gray_4d4d4d
                    )
                )
            } else {
                // Update the selected position
                selectedPosition = position
                holder.tvCouponCode.setTextColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.theme_blue_38B449
                    )
                )
            }

            // Refresh the list to reflect the changes
            notifyDataSetChanged()

            // Notify the listener with the selected item (or null if nothing is selected)
            val selectedItem = if (selectedPosition != -1) viewList[selectedPosition] else null
            listener.onCouponSelected(selectedItem)
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icCheckBox: CheckBox
        var tvCouponCode: TextView
        var tvSaveAmount: TextView
        var tvCouponDescription: TextView
        var tvCouponExpiry: TextView

        init {
            icCheckBox = itemView.findViewById(R.id.icCheckBox)
            tvCouponCode = itemView.findViewById(R.id.tvCouponCode)
            tvSaveAmount = itemView.findViewById(R.id.tvSaveAmount)
            tvCouponDescription = itemView.findViewById(R.id.tvCouponDescription)
            tvCouponExpiry = itemView.findViewById(R.id.tvCouponExpiry)
        }
    }
}