package com.matemart.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.interfaces.ApiInterface
import com.matemart.model.Labour
import com.matemart.model.LabourFilter
import com.matemart.model.ResGetLabour
import com.matemart.model.ResGetLabourFilter
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LabouresListActivity : AppCompatActivity() {

    var filterList: MutableList<LabourFilter> = arrayListOf()
    var labourList: MutableList<Labour> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laboures_list)

        getFilter()
        getLabour(0)
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
                    response.body()?.let { labourList.addAll(it.data) }
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
}