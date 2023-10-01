package com.matemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matemart.activities.onVariationChangeListener
import com.matemart.databinding.ItemVariationOuterBinding

class VariationOuterAdapter(
    var context: Context,
    private val variationsHashMap: HashMap<String, List<String>>,
    private val enabledVariationsHashMap: HashMap<String, List<String>>,
    private val selectedVariationsHashMap: HashMap<String, String>,private val listener : onVariationChangeListener
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

        val variationEntry: Map.Entry<String, List<String>>? =
            variationsHashMap.entries.elementAtOrNull(position)
        if (variationEntry != null) {
            val (key, valueList) = variationEntry


            holder.binding.tvVariationTitle.text = key

            holder.binding.rcVariation.layoutManager = LinearLayoutManager(
                context,
                RecyclerView.HORIZONTAL,
                false
            )
            holder.binding.rcVariation.adapter = VariationInnerAdapter(
                context,
                key,
                valueList,
                enabledVariationsHashMap,
                selectedVariationsHashMap,listener
            )
        }
    }

}