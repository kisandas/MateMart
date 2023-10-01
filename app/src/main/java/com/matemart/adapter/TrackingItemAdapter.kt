package com.matemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matemart.R
import com.matemart.activities.OrderTrackingActivity
import com.matemart.databinding.ItemLayoutArchitecturalProfessionalListBinding
import com.matemart.databinding.ItemOrderTrackingStatusAdapterBinding
import com.matemart.databinding.ItemReviewListBinding
import com.matemart.databinding.ItemVariationInnerBinding
import com.matemart.model.Architect
import com.matemart.model.OrderStatus
import com.matemart.model.Review

class TrackingItemAdapter(
    var context: Context,
    var list: List<OrderTrackingActivity.OrderStatusModel>
) :
    RecyclerView.Adapter<TrackingItemAdapter.MyViewHolder>() {


    class MyViewHolder(var binding: ItemOrderTrackingStatusAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemOrderTrackingStatusAdapterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == 0) {
            holder.binding.view1.visibility = INVISIBLE
        } else if (position == list.size - 1) {
            holder.binding.view2.visibility = INVISIBLE
        }

        if (list[position].isEnabled) {
            holder.binding.llview.visibility = VISIBLE
        } else {
            holder.binding.llview.visibility = INVISIBLE
        }

        holder.binding.tvStatusTitle.text = list[position].title
        holder.binding.tvDate.text = list[position].subTitle + " " + list[position].date



        if (list[position].isEnabled) {
            when (list[position].status) {
                0 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_placed_selected)
                }
                1 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_confirmed_selected)
                }
                2 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_placed_selected)
                }
                3 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_processed_selected)
                }
                4 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_ready_to_ship_selected)
                }
                5 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_delivered_selected)
                }
                6 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_return_selected)
                }
                -1 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_cancelled_selected)
                }
            }

        } else {

            when (list[position].status) {
                0 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_placed_unselected)
                }
                1 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_confirmed_unselected)
                }
                2 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_placed_unselected)
                }
                3 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_processed_unselected)
                }
                4 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_ready_to_ship_unselected)
                }
                5 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_delivered_unselected)
                }
                6 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_return_unselected)
                }
                -1 -> {
                    holder.binding.ivImage.setImageResource(R.drawable.ic_order_cancelled_unselected)
                }

            }
        }
    }


    public interface Item {
        fun onClick(architect: Architect)
    }
}