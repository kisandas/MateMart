package matemart.material.Interior.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import matemart.material.Interior.R
import matemart.material.Interior.activities.AddOrRemoveCartListener
import matemart.material.Interior.model.CartDataModel

class CartItemAdapter(
    private val viewList: ArrayList<CartDataModel>?,
    private val mContext: Context,private val cartListener: AddOrRemoveCartListener
) : RecyclerView.Adapter<CartItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_cart_product, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return viewList?.size ?: 0
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val item = viewList!![position]
        Glide.with(mContext).load(item.productImage).placeholder(R.drawable.place_holder_image)
            .into(holder.iv_product_image)
        holder.tv_product_name.text = item.pName
        holder.tv_price.text = "₹${item.saleprice}"
        val count = intArrayOf(0)

//        if (item.total_quantity!! <= 10) {
//            holder.tv_text_left_stock.text = "Only " + item.total_quantity + " in stock"
//        }
        holder.iv_minus.visibility = View.GONE
        holder.tv_count.visibility = View.GONE


        count[0] = item.qty!!


        if (count[0] > 0) {
            holder.iv_minus.visibility = View.VISIBLE
            holder.tv_count.visibility = View.VISIBLE
            holder.tv_count.text = "" + count[0]
        }


          holder.iv_plus.setOnClickListener {
              if (count[0] < item.totalQuantity!!) {
                  count[0]++
                  if (count[0] > 0) {
                      holder.iv_minus.visibility = View.VISIBLE
                      holder.tv_count.visibility = View.VISIBLE
                      holder.tv_count.text = "" + count[0]
                      cartListener.onAddCartAdded(item,count[0])
                      //                    call ApI for Add into cart
                  }
              } else {
                  Toast.makeText(
                      mContext,
                      "Only " + item.totalQuantity + " items available",
                      Toast.LENGTH_SHORT
                  ).show()
              }
          }


        holder.iv_minus.setOnClickListener {
            count[0]--
            if (count[0] < 1) {
                holder.iv_minus.visibility = View.GONE
                holder.tv_count.visibility = View.GONE

                cartListener.onCartRemoved(item)
//                    call ApI for Remove from cart
            } else {
                cartListener.onAddCartAdded(item,count[0])
//                addToCart(item, count[0])
            }

            holder.tv_count.text = "" + count[0]
        }
        holder.ivClose.setOnClickListener {
            cartListener.onCartRemoved(item)
//            removeFromCart(item)
        }
    }




    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_product_image: RoundedImageView
        var tv_product_name: TextView
         var tv_product_details: TextView
        var tv_price: TextView
        var iv_minus: ImageView
        var tv_count: TextView
         var iv_plus: ImageView
         var ivClose: ImageView


        init {

            tv_product_name = itemView.findViewById(R.id.tv_product_name)
            iv_product_image = itemView.findViewById(R.id.iv_product_image)
            tv_product_details = itemView.findViewById(R.id.tv_product_details)
            tv_price = itemView.findViewById(R.id.tv_price)
            iv_minus = itemView.findViewById(R.id.iv_minus)
            tv_count = itemView.findViewById(R.id.tv_count)
            iv_plus = itemView.findViewById(R.id.iv_plus)
            ivClose = itemView.findViewById(R.id.ivClose)


        }
    }
}