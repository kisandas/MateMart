package matemart.material.interior.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import matemart.material.interior.adapter.ProductItemAdapter
import matemart.material.interior.databinding.ActivityWhishListBinding
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.ResWishList
import matemart.material.interior.model.ViewListModel
import matemart.material.interior.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishListActivity : AppCompatActivity(),
    matemart.material.interior.interfaces.WishListUpdateListner {

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
