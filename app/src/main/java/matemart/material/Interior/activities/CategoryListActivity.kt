package matemart.material.Interior.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.example.CategoryModel
import com.example.example.ResponseCategory
import matemart.material.Interior.R
import matemart.material.Interior.interfaces.ApiInterface
import matemart.material.Interior.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryListActivity : AppCompatActivity() {

    lateinit var rvCategories: RecyclerView
    lateinit var ivBack: ImageView
    lateinit var adapter: matemart.material.Interior.adapter.CategoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        rvCategories = findViewById(R.id.rvCategories)
        ivBack = findViewById(R.id.iv_back)

        val layoutManager =
            GridLayoutManager(this, 2)
        rvCategories.layoutManager = layoutManager
        adapter = matemart.material.Interior.adapter.CategoryListAdapter(list, this, false)
        rvCategories.adapter = adapter


        ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        getAllCategory()
    }


    var list: ArrayList<CategoryModel> = arrayListOf()
    fun getAllCategory() {
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResponseCategory> = apiInterface.getAllCategory()!!

        call.enqueue(object : Callback<ResponseCategory> {
            override fun onResponse(
                call: Call<ResponseCategory>,
                response: Response<ResponseCategory>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { list.addAll(it) }
                    adapter!!.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@CategoryListActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseCategory>, t: Throwable) {
                Toast.makeText(
                    this@CategoryListActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }
}