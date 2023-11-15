package com.matemart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.matemart.model.AddCartResponse
import com.example.example.RemoveCartResponse
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.activities.CompareProductDetailsActivity
import com.matemart.activities.LoginActivity
import com.matemart.activities.ProductDetailsActivity
import com.matemart.interfaces.ApiInterface
import com.matemart.interfaces.WishListUpdateListner
import com.matemart.model.ViewListModel
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import com.matemart.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductItemAdapter(
    private var viewList: ArrayList<ViewListModel>?,
    private val mContext: Context,
    private val updateListner: WishListUpdateListner
) : RecyclerView.Adapter<ProductItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_product_adapter, parent, false)
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
        Glide.with(mContext).load(item.image).placeholder(R.drawable.img_loading_image)
            .into(holder.iv_product_image)
        holder.tvProductName.text = item.p_name
        holder.tvRating.text = item.average_rating
        holder.tv_text_left_stock.text = "" + item.total_quantity
        holder.tv_amount.text = "₹"+item.saleprice
        holder.tv_original_price.text = "₹"+item.price
        val count = intArrayOf(0)
        if (item.total_quantity!! <= 0) {
            holder.ll_bg_alpha.visibility = View.VISIBLE
            holder.rl_bg_alpha.visibility = View.VISIBLE
        } else {
            holder.ll_bg_alpha.visibility = View.GONE
            holder.rl_bg_alpha.visibility = View.GONE
        }
        if (item.total_quantity!! <= 10) {
            holder.tv_text_left_stock.text = "Only " + item.total_quantity + " in stock"
        }
        holder.iv_minus.visibility = View.GONE
        holder.tv_count.visibility = View.GONE

        if (item.cart != null) {
            count[0] = item.cart!!.qty!!
        }

        if(item.is_compare == 1){
            holder.qtyLayout.visibility = GONE
        }else{
            holder.qtyLayout.visibility = VISIBLE
        }

        if (item.is_wishlist == 0) {
            holder.iv_like_unlike_product.visibility = VISIBLE
            holder.iv_remove_whishlist.visibility = GONE
        } else {
            holder.iv_like_unlike_product.visibility = GONE
            holder.iv_remove_whishlist.visibility = VISIBLE
        }

        if (count[0] > 0) {
            holder.iv_minus.visibility = View.VISIBLE
            holder.tv_count.visibility = View.VISIBLE
            holder.tv_count.text = "" + count[0]
        }

        holder.iv_like_unlike_product.setOnClickListener {

            if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                    .read(SharedPrefHelper.IS_USER_GUEST, false)
            ) {
                Toast.makeText(
                    mContext,
                    Utils.LOGIN_MESSAGE,
                    Toast.LENGTH_LONG
                ).show()

                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

                pref.write(SharedPrefHelper.ADDRESS_ID, 0)
                pref.write(SharedPrefHelper.EMAIL, "")
                pref.write(SharedPrefHelper.USER_ID, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_NUMBER, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_TOKEN, "")
                pref.write(SharedPrefHelper.KEY_CCID, "")
                pref.write(SharedPrefHelper.KEY_LOGGED_IN, false)


                mContext.startActivity(Intent(mContext, LoginActivity::class.java))

            }else {
                updateWishList(item.p_id!!, 1, holder)
            }
        }

        holder.iv_remove_whishlist.setOnClickListener {
            if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                    .read(SharedPrefHelper.IS_USER_GUEST, false)
            ) {
                Toast.makeText(
                    mContext,
                    Utils.LOGIN_MESSAGE,
                    Toast.LENGTH_LONG
                ).show()

                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

                pref.write(SharedPrefHelper.ADDRESS_ID, 0)
                pref.write(SharedPrefHelper.EMAIL, "")
                pref.write(SharedPrefHelper.USER_ID, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_NUMBER, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_TOKEN, "")
                pref.write(SharedPrefHelper.KEY_CCID, "")
                pref.write(SharedPrefHelper.KEY_LOGGED_IN, false)


                mContext.startActivity(Intent(mContext, LoginActivity::class.java))

            }else {
                updateWishList(item.p_id!!, 0, holder)
            }
        }

        holder.iv_plus.setOnClickListener {

            if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                    .read(SharedPrefHelper.IS_USER_GUEST, false)
            ) {
                Toast.makeText(
                    mContext,
                    Utils.LOGIN_MESSAGE,
                    Toast.LENGTH_LONG
                ).show()

                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

                pref.write(SharedPrefHelper.ADDRESS_ID, 0)
                pref.write(SharedPrefHelper.EMAIL, "")
                pref.write(SharedPrefHelper.USER_ID, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_NUMBER, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_TOKEN, "")
                pref.write(SharedPrefHelper.KEY_CCID, "")
                pref.write(SharedPrefHelper.KEY_LOGGED_IN, false)


                mContext.startActivity(Intent(mContext, LoginActivity::class.java))

            }else {
                if (count[0] < item.total_quantity!!) {
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
                        "Only " + item.total_quantity + " items available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
        holder.itemView.setOnClickListener {


            if (item.is_compare == 0) {
                mContext.startActivity(
                    Intent(mContext, ProductDetailsActivity::class.java)
                        .putExtra("p_id", item.p_id)
                        .putExtra("product_detail_id", item.product_detail_id)
                )
            } else {
                mContext.startActivity(
                    Intent(mContext, CompareProductDetailsActivity::class.java)
                        .putExtra("p_id", item.p_id)
                        .putExtra("product_detail_id", item.product_detail_id)
                )
            }


            //                if (viewList.get(position).getClickID() != null && !viewList.get(position).getClickID().equalsIgnoreCase("null") && !viewList.get(position).getClickID().isEmpty()) {
//                    Utils.getMediaData(mContext, pref.getString(KEY_CCID), pref.getString(KEY_PROFILE_ID), pref.getString(KEY_THEME_ID), viewList.get(position).getClickID(), holder, false, true);
//                }
        }

        if(item.is_compare == 1){
            holder.qtyLayout.visibility = GONE
        }else{
            holder.qtyLayout.visibility = VISIBLE
        }
    }

    private fun addToCart(item: ViewListModel, count: Int) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", item.product_detail_id)
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
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        mContext,
                        response.body()?.message,
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

    fun updateWishList(productId: Int, is_wishlist: Int, holder: ItemViewHolder) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("p_id", productId)
        jsonObject.addProperty("is_wishlist", is_wishlist)
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, mContext)

        var call: Call<JsonObject> = apiInterface.updateWishList(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    if (is_wishlist == 0) {
                        holder.iv_like_unlike_product.visibility = VISIBLE
                        holder.iv_remove_whishlist.visibility = GONE
                    } else {
                        holder.iv_like_unlike_product.visibility = GONE
                        holder.iv_remove_whishlist.visibility = VISIBLE
                    }

                    updateListner.onUpdate()
                } else {
                    Toast.makeText(mContext, "Something went Wrong", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            }
        })


    }

    fun updateList(newList: List<ViewListModel>) {
        viewList = ArrayList(newList)
        notifyDataSetChanged()
    }

    private fun removeFromCart(item: ViewListModel) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", item.product_detail_id)


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
        var rlMainProduct: RelativeLayout
        var iv_product_image: ImageView
        var iv_remove_whishlist: ImageView
        var iv_like_unlike_product: ImageView
        var tvProductName: TextView
        var tvRating: TextView
        var tv_text_left_stock: TextView
        var tv_amount: TextView
        var tv_original_price: TextView
        var iv_minus: ImageView
        var iv_plus: ImageView
        var tv_count: TextView
        var qtyLayout: RelativeLayout

        //        out of stock
        var ll_bg_alpha: LinearLayout
        var rl_bg_alpha: RelativeLayout
        var tvOutOfStock: TextView
        var viewSimilarProduct: TextView

        init {
            rlMainProduct = itemView.findViewById(R.id.rlMainProduct)
            iv_product_image = itemView.findViewById(R.id.iv_product_image)
            iv_remove_whishlist = itemView.findViewById(R.id.iv_remove_whishlist)
            iv_like_unlike_product = itemView.findViewById(R.id.iv_like_unlike_product)
            tvProductName = itemView.findViewById(R.id.tvProductName)
            tvRating = itemView.findViewById(R.id.tvRating)
            tv_text_left_stock = itemView.findViewById(R.id.tv_text_left_stock)
            tv_amount = itemView.findViewById(R.id.tv_amount)
            tv_original_price = itemView.findViewById(R.id.tv_original_price)
            iv_minus = itemView.findViewById(R.id.iv_minus)
            iv_plus = itemView.findViewById(R.id.iv_plus)
            tv_count = itemView.findViewById(R.id.tv_count)
            qtyLayout = itemView.findViewById(R.id.qtyLayout)
            ll_bg_alpha = itemView.findViewById(R.id.ll_bg_alpha)
            rl_bg_alpha = itemView.findViewById(R.id.rl_bg_alpha)
            tvOutOfStock = itemView.findViewById(R.id.tvOutOfStock)
            viewSimilarProduct = itemView.findViewById(R.id.viewSimilarProduct)
        }
    }
}