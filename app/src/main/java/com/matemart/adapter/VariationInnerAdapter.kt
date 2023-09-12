package com.matemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matemart.databinding.ItemLayoutArchitecturalProfessionalListBinding
import com.matemart.databinding.ItemReviewListBinding
import com.matemart.databinding.ItemVariationInnerBinding
import com.matemart.model.Architect
import com.matemart.model.Review

class VariationInnerAdapter(
    var context: Context,
    var list: List<String>
) :
    RecyclerView.Adapter<VariationInnerAdapter.MyViewHolder>() {


    class MyViewHolder(var binding: ItemVariationInnerBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemVariationInnerBinding.inflate(
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

        holder.binding.tvVariationName.text = list[position]

    }


    public interface Item {
        fun onClick(architect: Architect)
    }
}