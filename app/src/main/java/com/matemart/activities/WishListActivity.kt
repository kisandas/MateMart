package com.matemart.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.matemart.adapter.ProductItemAdapter
import com.matemart.adapter.WishLIstAdapter
import com.matemart.databinding.ActivityWhishListBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.interfaces.WishListUpdateListner
import com.matemart.model.ResWishList
import com.matemart.model.ViewListModel
import com.matemart.model.WishList
import com.matemart.repository.UpdateWishList
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishListActivity : AppCompatActivity(), WishListUpdateListner {

    lateinit var binding: ActivityWhishListBinding;
    var list: ArrayList<ViewListModel> = arrayListOf()

    lateinit var adapter: ProductItemAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhishListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.include3.title.text = "Wishlist"
        binding.include3.ivBack.setOnClickListener() {
            onBackPressed()
        }
        adapter = ProductItemAdapter(list,this@WishListActivity,this)
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

    override fun onUpdate() {
        getWishList()

    }


}
