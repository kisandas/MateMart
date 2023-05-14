package com.matemart.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.matemart.adapter.WishLIstAdapter
import com.matemart.databinding.ActivityWhishListBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.ResWishList
import com.matemart.model.WishList
import com.matemart.repository.UpdateWishList
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishListActivity : AppCompatActivity() {

    lateinit var binding: ActivityWhishListBinding;
    var list: MutableList<WishList> = arrayListOf()

    lateinit var adapter: WishLIstAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhishListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.include3.title.text = "Wishlist"
        binding.include3.ivBack.setOnClickListener() {
            onBackPressed()
        }
        adapter = WishLIstAdapter(this@WishListActivity, list, object : WishLIstAdapter.OnClick {
            override fun onDelete(wishList: WishList) {
                wishList.p_id?.let {
                    UpdateWishList(this@WishListActivity, object : UpdateWishList.Event {
                        override fun OnUpdate() {
                            getWishList()
                        }

                        override fun OnFail() {
                            Toast.makeText(
                                this@WishListActivity,
                                "Something went wrong",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }

                    }).updateWishList(it, 0)
                }
            }

        })
        binding.list.adapter = adapter
        getWishList()

    }


    fun getWishList() {
        list.clear()
        adapter.notifyDataSetChanged()
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResWishList> = apiInterface.getWishList()!!

        call.enqueue(object : Callback<ResWishList> {
            override fun onResponse(call: Call<ResWishList>, response: Response<ResWishList>) {
                if (response.isSuccessful) {
                    response.body()!!.data?.let { list.addAll(it) }
                    adapter.notifyDataSetChanged()
//                    list.addAll(response.body())
                } else {
                    Toast.makeText(
                        this@WishListActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResWishList>, t: Throwable) {
                Toast.makeText(this@WishListActivity, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }

        })

    }


}
