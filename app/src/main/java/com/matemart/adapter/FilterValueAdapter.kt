package com.matemart.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.matemart.R
import com.matemart.model.FilterBody


class FilterValueAdapter(
    private var valueList: ArrayList<FilterBody>,
    private var keyValue: String,
    private val onClickValue: (key: String, data: FilterBody, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<FilterValueAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val view = layoutInflater.inflate(R.layout.item_rc_value_inner, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.valueList.size
    }

    fun setData(valueList: List<FilterBody>, keyValue: String) {
        this.valueList.clear()
        Log.e("checkSizeeeee", "clear size: "+this.valueList.size )
        this.valueList.addAll(valueList)
        this.keyValue = keyValue
        notifyDataSetChanged()
        Log.e("checkSizeeeee", "set size: " +  this.valueList.size)

//
    }

    // This method is called when binding the data to the views being created in RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(valueList[position])
    }

    // This is your ViewHolder class that helps to populate data to the view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvState: TextView
        val icCheckBox: CheckBox

        init {
            tvState = itemView.findViewById(R.id.tvState)
            icCheckBox = itemView.findViewById(R.id.icCheckBox)

        }

        fun bindData(filterBody: FilterBody) {

            tvState.text = filterBody.name

            icCheckBox.isChecked =filterBody.isDisabled

            tvState.setOnClickListener {
                icCheckBox.isChecked = !icCheckBox.isChecked
                valueList[adapterPosition].isDisabled = icCheckBox.isChecked
            }
            itemView.setOnClickListener {
                icCheckBox.isChecked = !icCheckBox.isChecked
                valueList[adapterPosition].isDisabled = icCheckBox.isChecked
            }
            icCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

                Log.e("checkkkk----==========>1111", "position clicked: " + keyValue.toString())
                onClickValue(keyValue,filterBody, isChecked)
            }
        }
    }
}