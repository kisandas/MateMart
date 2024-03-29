package matemart.material.interior.activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.example.CategoryModel
import com.example.example.ResponseCategory
import com.google.gson.JsonObject
import matemart.material.interior.R
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubCategoryActivityList : AppCompatActivity() {

    lateinit var tvTitle: TextView
    lateinit var rvSubCategory: RecyclerView
    lateinit var iv_back: ImageView
    lateinit var adapter: matemart.material.interior.adapter.CategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category_list)

        tvTitle = findViewById(R.id.tvTitle)
        rvSubCategory = findViewById(R.id.rvSubCategory)
        iv_back = findViewById(R.id.iv_back)

        iv_back.setOnClickListener { onBackPressedDispatcher.onBackPressed() }


        tvTitle.text = intent.getStringExtra("title")
        val layoutManager =
            GridLayoutManager(this, 2)
        rvSubCategory.layoutManager = layoutManager
        adapter = matemart.material.interior.adapter.CategoryListAdapter(list, this, true)
        rvSubCategory.adapter = adapter
        getSubCategory(intent.getIntExtra("c_id", 0))
    }

    var list: ArrayList<CategoryModel> = arrayListOf()
    private fun getSubCategory(c_id: Int) {
        var jsonObject = JsonObject()
        jsonObject.addProperty("c_id", c_id)
        Log.e("mmmmmm", "getSubCategory: " + jsonObject.toString())


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@SubCategoryActivityList)
        var call: Call<ResponseCategory> = apiInterface.getSubCategory(jsonObject)!!

        call.enqueue(object : Callback<ResponseCategory> {
            override fun onResponse(
                call: Call<ResponseCategory>,
                response: Response<ResponseCategory>
            ) {
                if (response.body()?.statuscode == 11) {
                    response.body()?.data?.let { list.addAll(it) }
                    adapter!!.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@SubCategoryActivityList,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseCategory>, t: Throwable) {
                Toast.makeText(
                    this@SubCategoryActivityList,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }

}