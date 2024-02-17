package matemart.material.Interior.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import matemart.material.Interior.databinding.ItemLayoutOrderProductListBinding
import matemart.material.Interior.model.OrderDetail

class ItemOrderDetailsListAdapter(
    var context: Context,
    var list: MutableList<OrderDetail>,
    var itemListener: ItemClickListener
) :
    RecyclerView.Adapter<ItemOrderDetailsListAdapter.MyViewHolder>() {


    class MyViewHolder(var binding: ItemLayoutOrderProductListBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemLayoutOrderProductListBinding.inflate(
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
        holder.binding.tvProductName.text = list[position].productDetail?.pName
        holder.binding.tvPrice.text = "₹${list[position].price}"
        holder.binding.tvQty.text = "${list[position].qty}"
        Glide.with(context)
            .load(list[position].productDetail?.image).into(holder.binding.ivImage)

        holder.itemView.setOnClickListener {
            itemListener.onClick(list[position])
        }

    }


    public interface ItemClickListener {
        fun onClick(orderData: OrderDetail)
    }
}