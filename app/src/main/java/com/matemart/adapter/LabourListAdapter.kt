package com.matemart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.matemart.activities.ContactForm
import com.matemart.databinding.ItemLayoutLaboureListBinding
import com.matemart.model.Labour

class LabourListAdapter(var context: Context,var list: MutableList<Labour>) :
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

        holder.itemView.setOnClickListener {
            context.startActivity(
                Intent(context, ContactForm::class.java).putExtra("pro_id",list.get(position).id)
                .putExtra("type","labour"))
        }

    }
}