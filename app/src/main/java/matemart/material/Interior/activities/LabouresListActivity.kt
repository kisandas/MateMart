package matemart.material.Interior.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import matemart.material.Interior.adapter.LabourListAdapter
import matemart.material.Interior.databinding.ActivityLabouresListBinding
import matemart.material.Interior.fragments.LabourFilterBottomSheet
import matemart.material.Interior.interfaces.ApiInterface
import matemart.material.Interior.interfaces.DismissBottomSheet
import matemart.material.Interior.model.Labour
import matemart.material.Interior.model.LabourFilter
import matemart.material.Interior.model.ResGetLabour
import matemart.material.Interior.model.ResGetLabourFilter
import matemart.material.Interior.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LabouresListActivity : AppCompatActivity(), DismissBottomSheet {

    var filterList: MutableList<LabourFilter> = arrayListOf()
    var labourList: MutableList<Labour> = arrayListOf()
    lateinit var binding: ActivityLabouresListBinding;
    lateinit var adapter: LabourListAdapter;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLabouresListBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        SharedPrefHelper.getInstance(MyApplication.getInstance()).write(
//            SharedPrefHelper.KEY_ACCESS_TOKEN,
//            "KzkxNTU1NTU1NTU1NToyMDIzMDUwMTIwMTEzOFZtVGpvbWpLbG9xbHVjSUlCZVhn"
//        )

        binding.headerlay.ivBack.setOnClickListener() {
            finish()
        }
        binding.headerlay.title.text = "Laboures"
        adapter = LabourListAdapter(this@LabouresListActivity,labourList)
        binding.list.adapter = adapter
        getFilter()
        getLabour(0)

        binding.filter.setOnClickListener() {
            var labourFilterBottomSheet: LabourFilterBottomSheet

            labourFilterBottomSheet = LabourFilterBottomSheet(filterList, this@LabouresListActivity)
            labourFilterBottomSheet.show(supportFragmentManager, "bottomsheet")
        }
    }

    fun getFilter() {
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResGetLabourFilter> = apiInterface.getLabourFilter()!!

        call.enqueue(object : Callback<ResGetLabourFilter> {
            override fun onResponse(
                call: Call<ResGetLabourFilter>,
                response: Response<ResGetLabourFilter>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { filterList.addAll(it.data) }
                } else {
                    Toast.makeText(
                        this@LabouresListActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetLabourFilter>, t: Throwable) {
                Toast.makeText(this@LabouresListActivity, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }

        })

    }

    fun getLabour(type: Int) {
        labourList.clear()
        adapter.notifyDataSetChanged()
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("type", type)
        var call: Call<ResGetLabour> = apiInterface.getAllLabour(jsonObject)!!

        call.enqueue(object : Callback<ResGetLabour> {
            override fun onResponse(
                call: Call<ResGetLabour>,
                response: Response<ResGetLabour>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        labourList.addAll(it.data)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(
                        this@LabouresListActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetLabour>, t: Throwable) {
                Toast.makeText(this@LabouresListActivity, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }

        })

    }

    override fun DismissDialog(position: Int, state: String?, type: String?) {
        var int: Int = 0
        for (filter in filterList) {
            if (filter.name.equals(state)) {
                int = filter.id!!
            }
        }
        getLabour(int)
    }
}