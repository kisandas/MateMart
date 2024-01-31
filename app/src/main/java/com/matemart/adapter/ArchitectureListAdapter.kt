package com.matemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matemart.R
import com.matemart.databinding.ItemLayoutArchitecturalProfessionalListBinding
import com.matemart.model.Architect

class ArchitectureListAdapter(
    var context: Context,
    var list: MutableList<Architect>,
    var item: Item
) :
    RecyclerView.Adapter<ArchitectureListAdapter.MyViewHolder>() {


    class MyViewHolder(var binding: ItemLayoutArchitecturalProfessionalListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemLayoutArchitecturalProfessionalListBinding.inflate(
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
        holder.binding.tvName.text = list.get(position).firm_name
        holder.binding.tvFirmName.text = list.get(position).name
        Glide.with(context).load(list.get(position).profile_image).placeholder(R.drawable.place_holder_image).into(holder.binding.image)
        holder.binding.root.setOnClickListener { item.onClick(list.get(position)) }
    }


    public interface Item {
        fun onClick(architect: Architect)
    }
}