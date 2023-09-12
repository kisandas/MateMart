package com.matemart.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matemart.databinding.ItemLayoutArchitecturalProfessionalListBinding
import com.matemart.databinding.ItemReviewListBinding
import com.matemart.databinding.ItemVariationOuterBinding
import com.matemart.model.Architect
import com.matemart.model.Review

 class VariationOuterAdapter(
    var context: Context,
   private val variationsHashMap: HashMap<String, List<String>>
) :
    RecyclerView.Adapter<VariationOuterAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemVariationOuterBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemVariationOuterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return variationsHashMap.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val variationEntry: Map.Entry<String, List<String>>? = variationsHashMap.entries.elementAtOrNull(position)
        if (variationEntry != null) {
            val (key, valueList) = variationEntry


            holder.binding.tvVariationTitle.text = key

            holder.binding.rcVariation.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.HORIZONTAL,
                false
            )
            holder.binding.rcVariation.adapter =  VariationInnerAdapter(
                context,
                valueList
            )
        }




    }


    public interface Item {
        fun onClick(architect: Architect)
    }
}