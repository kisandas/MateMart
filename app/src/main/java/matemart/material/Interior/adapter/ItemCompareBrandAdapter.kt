package matemart.material.Interior.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import matemart.material.Interior.R
import matemart.material.Interior.activities.ProductVariationClickItem
import matemart.material.Interior.model.CompareResult

class ItemCompareBrandAdapter(
    private val mContext: Context,
    private val variationList: ArrayList<CompareResult>?,private val listener: ProductVariationClickItem
) : RecyclerView.Adapter<ItemCompareBrandAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view =
            layoutInflater.inflate(R.layout.item_compare_brand_adapter, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return variationList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = variationList!![position]
        if (position % 2 == 0) {
            holder.parentLayout.setBackgroundColor(mContext.getColor(R.color.gray_eeeeee))

        } else {
            holder.parentLayout.setBackgroundColor(mContext.getColor(R.color.white))
        }

        holder.tvBrandName.text = item.brandName
        holder.tvPriceName.text = ""+item.total


        val resultString = StringBuilder()

        for (i in 0 until item.notCompare.size) {
            val lineItem = item.notCompare[i]
            resultString.append(lineItem)

            // Add a newline after each item except the last one
            if (i < item.notCompare.size - 1) {
                resultString.append("\n")
            }
        }

        val finalResult = resultString.toString()

        holder.tvNoteDetails.text = finalResult

        holder.btnAddToCart.setOnClickListener { listener.onAddItemIntoCart(position,item) }


    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentLayout: LinearLayout
        val tvBrandName: TextView
        val tvPriceName: TextView
        val tvNoteDetails: TextView
        val btnAddToCart: TextView

        init {
            parentLayout = itemView.findViewById(R.id.parentLayout)
            tvBrandName = itemView.findViewById(R.id.tvBrandName)
            tvNoteDetails = itemView.findViewById(R.id.tvNoteDetails)
            tvPriceName = itemView.findViewById(R.id.tvPriceName)
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart)
        }

    }


}