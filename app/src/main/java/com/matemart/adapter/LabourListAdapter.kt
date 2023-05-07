package com.matemart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.matemart.databinding.ItemLayoutLaboureListBinding
import com.matemart.model.Labour

class LabourListAdapter(var list: MutableList<Labour>) :
    RecyclerView.Adapter<LabourListAdapter.MyViewHolder>() {


    class MyViewHolder(var binding: ItemLayoutLaboureListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemLayoutLaboureListBinding.inflate(
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
        holder.binding.tvName.text = list.get(position).name
        holder.binding.tvLocality.text = list.get(position).locality
        holder.binding.tvPhone.text = list.get(position).profession

    }
}