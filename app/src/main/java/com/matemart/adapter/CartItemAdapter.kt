package com.matemart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.example.AddCartResponse
import com.example.example.RemoveCartResponse
import com.google.gson.JsonObject
import com.makeramen.roundedimageview.RoundedImageView
import com.matemart.R
import com.matemart.interfaces.ApiInterface
import com.matemart.interfaces.WishListUpdateListner
import com.matemart.model.CartDataModel
import com.matemart.model.ViewListModel
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartItemAdapter(
    private val viewList: ArrayList<CartDataModel>?,
    private val mContext: Context
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
        Glide.with(mContext).load(item.productImage).placeholder(R.drawable.img_loading_image)
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
                      addToCart(item, count[0])
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
                removeFromCart(item)
//                    call ApI for Remove from cart
            } else {
                addToCart(item, count[0])
            }

            holder.tv_count.text = "" + count[0]
        }
        holder.ivClose.setOnClickListener {
            removeFromCart(item)
        }
    }

    private fun addToCart(item: CartDataModel, count: Int) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", item.productDetailId)
        jsonObject.addProperty("qty", count)
        jsonObject.addProperty("sample", 0)

        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, mContext)
        var call: Call<AddCartResponse> = apiInterface.addIntoCart(jsonObject)!!

        call.enqueue(object : Callback<AddCartResponse> {
            override fun onResponse(
                call: Call<AddCartResponse>,
                response: Response<AddCartResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    Toast.makeText(
                        mContext,
                        "Item Added to Cart",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        mContext,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                Toast.makeText(
                    mContext,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }


    private fun removeFromCart(item: CartDataModel) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", item.productDetailId)


        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, mContext)
        var call: Call<RemoveCartResponse> = apiInterface.removeFromCart(jsonObject)!!

        call.enqueue(object : Callback<RemoveCartResponse> {
            override fun onResponse(
                call: Call<RemoveCartResponse>,
                response: Response<RemoveCartResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    Toast.makeText(
                        mContext,
                        "Item Removed from Cart",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        mContext,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<RemoveCartResponse>, t: Throwable) {
                Toast.makeText(
                    mContext,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

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