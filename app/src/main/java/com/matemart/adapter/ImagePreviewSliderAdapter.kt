package com.matemart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matemart.R

class ImagePreviewSliderAdapter(private val context: Context, private val itemList: List<String>,private val position:Int, private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ImagePreviewSliderAdapter.ViewHolder>() {

    private var selectedPosition = position

    interface OnItemClickListener {
        fun onItemClick(position: Int,itemList:List<String>)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        val ivSelector: ImageView = itemView.findViewById(R.id.ivSelector)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClick(position, itemList)
                    setSelectedPosition(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_image_preview_slider_adapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context).load(itemList[position])
            .placeholder(R.drawable.img_loading_image)
            .into(holder.ivImage)

        if(selectedPosition == position){
           holder.ivSelector.visibility = VISIBLE
       }else{
           holder.ivSelector.visibility = GONE  }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

     fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }




}


