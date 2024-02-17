package matemart.material.Interior.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import matemart.material.Interior.R
import matemart.material.Interior.activities.ProductDetailsActivity.Companion.finalSelectedVariation
import matemart.material.Interior.activities.onVariationChangeListener
import matemart.material.Interior.databinding.ItemVariationInnerBinding

class VariationInnerAdapter(
    var context: Context,
    var keyName: String,
    var list: List<String>,
    private val enabledVariationsHashMap: HashMap<String, List<String>>,
    private val selectedVariationsHashMap: HashMap<String, String>,private val listener : onVariationChangeListener
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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.tvVariationName.text = list[position]
        val enabledFilterList = enabledVariationsHashMap[keyName]?.let { ArrayList(it) }

        val selectedVariation = HashMap<String, String>()


        holder.binding.tvVariationName.setOnClickListener {
            if (enabledFilterList != null) {
                if (enabledFilterList.contains(list[position])) {
                    selectedVariation[keyName] = list[position]
                    finalSelectedVariation[keyName] = list[position]
                    listener.onVariationChanged(finalSelectedVariation)
                }
            }

//
        }

        if (enabledFilterList != null) {
            if (enabledFilterList.contains(list[position])) {

                holder.binding.tvVariationName.isEnabled = true

                if(selectedVariationsHashMap.containsValue(list[position])){
                    holder.binding.tvVariationName.isChecked = true
                    finalSelectedVariation[keyName] = list[position]



                }else{
                    holder.binding.tvVariationName.isChecked = false
                }

                holder.binding.tvVariationName.background =
                    context.getDrawable(R.drawable.textview_selector)

            } else {

                holder.binding.tvVariationName.isEnabled = false
                holder.binding.tvVariationName.setBackgroundResource(R.drawable.item_bg_round_corner_with_stroke_disabled_radius_6)

            }
        }
    }


}