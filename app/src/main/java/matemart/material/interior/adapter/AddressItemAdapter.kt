package matemart.material.interior.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import matemart.material.interior.R
import matemart.material.interior.model.Address

class AddressItemAdapter(
    private val mContext: Context,
    private val addressList: ArrayList<Address>?,
    private val listener: ItemClickListener,
) : RecyclerView.Adapter<AddressItemAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_layout_address_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return addressList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = addressList!![position]

        holder.tvName.text = item.fullname
        holder.tvAddress.text = item.address
        holder.tvPhone.text = "Phone No: "+item.aMobile

        if (item.default == 1) {
            holder.tvDefault.visibility = VISIBLE
            holder.tvMarkAsDefault.visibility = GONE
        } else {
            holder.tvDefault.visibility = INVISIBLE
            holder.tvMarkAsDefault.visibility = VISIBLE
        }

        holder.tvMarkAsDefault.setOnClickListener {
            listener.onItemSetAsDefault(item)
        }

        holder.tvEdit.setOnClickListener {
            listener.onItemEdit(item)
        }

        holder.tvDelete.setOnClickListener {
            listener.onItemDelete(item)
        }


    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView
        val tvDefault: TextView
        val tvAddress: TextView
        val tvPhone: TextView
        val tvMarkAsDefault: TextView
        val tvDelete: TextView
        val tvEdit: TextView

        init {
            tvName = itemView.findViewById(R.id.tvName)
            tvDefault = itemView.findViewById(R.id.tvDefault)
            tvAddress = itemView.findViewById(R.id.tvAddress)
            tvPhone = itemView.findViewById(R.id.tvPhone)
            tvMarkAsDefault = itemView.findViewById(R.id.tvMarkAsDefault)
            tvDelete = itemView.findViewById(R.id.tvDelete)
            tvEdit = itemView.findViewById(R.id.tvEdit)
        }


    }

    interface ItemClickListener {
        fun onItemEdit(address: Address)
        fun onItemDelete(address: Address)
        fun onItemSetAsDefault(address: Address)
    }
}