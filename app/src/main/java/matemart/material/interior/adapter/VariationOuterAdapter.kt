package matemart.material.interior.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import matemart.material.interior.activities.onVariationChangeListener
import matemart.material.interior.databinding.ItemVariationOuterBinding

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


            val sortedList = valueList.sortedWith(Comparator { str1, str2 ->
                when {
                    str1.all { it.isDigit() } && str2.all { it.isDigit() } -> {
                        str2.toInt() - str1.toInt() // Reverse order for numbers
                    }
                    str1.all { it.isLetter() } && str2.all { it.isLetter() } -> {
                        str2.compareTo(str1, ignoreCase = true) // Reverse order for strings
                    }
                    else -> {
                        if (str1.all { it.isDigit() }) -1 else 1 // Reverse order for mixed types
                    }
                }
            })

            holder.binding.rcVariation.adapter = VariationInnerAdapter(
                context,
                key,
                sortedList,
                enabledVariationsHashMap,
                selectedVariationsHashMap,listener
            )
        }
    }

}