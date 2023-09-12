package com.matemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matemart.databinding.ItemLayoutArchitecturalProfessionalListBinding
import com.matemart.databinding.ItemRatingBarLayoutBinding
import com.matemart.databinding.ItemReviewListBinding
import com.matemart.model.Architect
import com.matemart.model.Rating
import com.matemart.model.Review

class RatingBarListAdapter(
    var context: Context,
    var list: List<Rating>,
    var totalRating:Double
) :
    RecyclerView.Adapter<RatingBarListAdapter.MyViewHolder>() {


    class MyViewHolder(var binding: ItemRatingBarLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemRatingBarLayoutBinding.inflate(
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
        holder.binding.Rating.text = list[position].rating
        holder.binding.ratingCount.text = list[position].count.toString()
        holder.binding.progressRating.max = totalRating.toInt()
        holder.binding.progressRating.progress = list[position].count

    }


    public interface Item {
        fun onClick(architect: Architect)
    }
}