package com.matemart.activities


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.matemart.activities.SearchProductFromCategoryActivity.Companion.actualMap
import com.matemart.activities.SearchProductFromCategoryActivity.Companion.selectedMap
import com.matemart.adapter.FilterKeyAdapter
import com.matemart.adapter.FilterValueAdapter
import com.matemart.databinding.ActivityFilterBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.*
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FilterActivity : AppCompatActivity() {
    lateinit var binding: ActivityFilterBinding
    var c_id = 0
    var clickId = ""

    var currentKey = ""
    var currentValue: List<FilterBody> = emptyList()

    lateinit var keyAdapter: FilterKeyAdapter
    lateinit var valueAdapter: FilterValueAdapter


    var keyList: ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedMap?.clear()
        c_id = intent.getIntExtra("c_id", 0)
        clickId = intent.getStringExtra("clickId").toString()
        if (clickId.isEmpty() || clickId == "null") {
            clickId = "Category"
        }

        binding.textViewApplyFilter.setOnClickListener {

            val intent = Intent()
//            intent.putExtra("filterMap",actualMap)
            intent.putExtra("c_id", c_id)
            intent.putExtra("clickId", clickId)
            setResult(RESULT_OK, intent)
            finish()

        }
        binding.ivBack.setOnClickListener { finish() }
        binding.tvReset.setOnClickListener {
            actualMap?.clear()
            finish()
        }



        binding.etSearchProduct.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called when the text changes, you can call the filterList function here


                if (s.toString().isEmpty()) {
//                    keyAdapter.setData(actualMap!!, actualMap!!.keys.toList()[0])

                    valueAdapter.setData(
                        ArrayList(actualMap!![actualMap!!.keys.toList()[0]]!!),
                        actualMap!!.keys.toList()[0]
                    )
                } else {
                    val query = s.toString()
                    filterList(query)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed in this case
            }
        })

        keyAdapter = FilterKeyAdapter(this@FilterActivity, LinkedHashMap(), "") { key, data ->
            valueAdapter.setData(data!!, key)
            currentKey = key
            currentValue = data
        }

        valueAdapter = FilterValueAdapter(ArrayList(), "") { key, data, isSelected ->

            if (actualMap != null && actualMap?.isNotEmpty() == true) {
//                actualMap?.get(key)?.find { it.name == data.name }?.isDisabled = isSelected
                keyAdapter.setData(actualMap!!, key)
            }

            var filterList: ArrayList<FilterBody> = arrayListOf()
            val list = selectedMap?.get(key)
            if (isSelected) {
                if (list is ArrayList<*> && list.isNotEmpty()) {
                    filterList = list as ArrayList<FilterBody>
                    Log.e("mmmmm", "onCreate:get-filter--- " + filterList.size + "   kk")
                    filterList.add(data)

                } else {
                    filterList.add(data)
                    selectedMap?.put(key, filterList)
                    // Handle the case when the list is null or empty
                }
            } else {
                if (list is ArrayList<*> && list.isNotEmpty()) {
                    filterList = list as ArrayList<FilterBody>
                    Log.e("mmmmm", "onCreate:get-filter--- " + filterList.size + "   kk")
                    filterList.removeIf { it.name == data.name }

                } else {
                    filterList.removeIf { it.name == data.name }
                    selectedMap?.put(key, filterList)
                    // Handle the case when the list is null or empty
                }
            }
            getSelectedFilter(c_id, clickId, key)
            Log.e("mmmmmm", "onCreate:get-filter " + selectedMap?.get(key) + "   kk")

            Log.e("checkkkk----==========>", "onResponse: " + actualMap.toString())

        }

        binding.filterKey.apply {
            layoutManager = LinearLayoutManager(this@FilterActivity)
            adapter = keyAdapter
        }

        binding.filterValue.apply {
            layoutManager = LinearLayoutManager(this@FilterActivity)
            adapter = valueAdapter
        }


        if (actualMap?.isNotEmpty() == true) {
            keyAdapter.setData(actualMap!!, actualMap!!.keys.toList()[0])

            valueAdapter.setData(
                ArrayList(actualMap!![actualMap!!.keys.toList()[0]]!!),
                actualMap!!.keys.toList()[0]
            )

        } else {

            getAllFilter(c_id, clickId)
        }

    }


    fun filterList(query: String) {
        val filteredList =
            currentValue.filter { item -> item.name.toString().contains(query, ignoreCase = true) }
        valueAdapter.setData(filteredList, currentKey)
    }

    fun getAllFilter(c_id: Int, clickId: String) {

        var jsonObject = JsonObject()
        if (clickId.isEmpty()) {
            jsonObject.addProperty("clickId", "category")
        } else {
            jsonObject.addProperty("clickId", clickId)
        }

        if (c_id != 0) {
            jsonObject.addProperty("c_id", c_id)
        }

        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<FilterModelClass> = apiInterface.getAllFilter(jsonObject)!!

        call.enqueue(object : Callback<FilterModelClass> {
            override fun onResponse(
                call: Call<FilterModelClass>,
                response: Response<FilterModelClass>
            ) {
                if (response.isSuccessful) {

                    var innerMap: LinkedHashMap<String, LinkedHashMap<String, List<FilterBody>>>? =
                        response.body()?.data

                    actualMap?.clear()


                    if (innerMap != null) {

                        innerMap.values.forEachIndexed { index, linkedHashMap ->
                            actualMap?.put(
                                linkedHashMap.keys.toList()[0],
                                linkedHashMap[linkedHashMap.keys.toList()[0]]!!
                            )

                        }

                        currentKey = actualMap!!.keys.toList()[0]
                        currentValue = actualMap!![actualMap!!.keys.toList()[0]]!!

                        keyAdapter.setData(actualMap!!, actualMap!!.keys.toList()[0])

                        valueAdapter.setData(
                            ArrayList(actualMap!![actualMap!!.keys.toList()[0]]!!),
                            actualMap!!.keys.toList()[0]
                        )


                    }


                } else {
                    Log.e("mmmmmkkkkkkkkkkkk", "API Call Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FilterModelClass>, t: Throwable) {
                Log.e("mmmmmkkkkkkkkkkkk", "API Call Failed: ${t.message}")
                t.printStackTrace()
                // Add any additional error handling code here
                Toast.makeText(
                    this@FilterActivity,
                    "API Call Failed: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })


    }

    fun convertToJSON(inputMap: HashMap<String, List<FilterBody>>): JsonObject {
        val jsonObject = JsonObject()

        for ((key, valueList) in inputMap) {
            val nameArray = JsonArray()

            for (filterBody in valueList) {
                nameArray.add(filterBody.name.toString())
            }

            if (nameArray.size() > 0)
                jsonObject.add(key, nameArray)

        }

        if (SearchProductFromCategoryActivity.minPrice != 0.0 || SearchProductFromCategoryActivity.maxPrice != 0.0) {
            val priceArray = JsonArray()
            priceArray.add(SearchProductFromCategoryActivity.minPrice)
            priceArray.add(SearchProductFromCategoryActivity.maxPrice)
            jsonObject.add("price", priceArray)
        }

        return jsonObject
    }

    fun getSelectedFilter(c_id: Int, clickId: String, key: String) {

        val obj = convertToJSON(selectedMap!!)

        if (clickId.isNullOrEmpty()) {
            obj.addProperty("clickId", "category")
        } else {
            obj.addProperty("clickId", clickId)
        }

        if (c_id != 0) {
            obj.addProperty("c_id", c_id)
        }

        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<FilterModelClass> = apiInterface.getSelectedAllFilter(obj)!!

        call.enqueue(object : Callback<FilterModelClass> {
            override fun onResponse(
                call: Call<FilterModelClass>,
                response: Response<FilterModelClass>
            ) {
                if (response.isSuccessful) {

                    var innerMap: LinkedHashMap<String, LinkedHashMap<String, List<FilterBody>>>? = response.body()?.data

                    actualMap?.clear()



                    if (innerMap != null) {
                        innerMap.values.forEachIndexed { index, linkedHashMap ->
                            actualMap?.put(
                                linkedHashMap.keys.toList()[0],
                                linkedHashMap[linkedHashMap.keys.toList()[0]]!!
                            )
                            if (index == 2) {
                                Log.e(
                                    "chhhjjhjhjh",
                                    "onResponse: " + linkedHashMap[linkedHashMap.keys.toList()[0]].toString()
                                )
                            }
                        }


                        currentKey = key
                        currentValue = actualMap!![key]!!

                        keyAdapter.setData(actualMap!!, key)

                        valueAdapter.setData(
                            ArrayList(actualMap!![key]!!),
                            key
                        )


                    }


                } else {
                    Log.e("mmmmmkkkkkkkkkkkk", "API Call Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<FilterModelClass>, t: Throwable) {
                Log.e("mmmmmkkkkkkkkkkkk", "API Call Failed: ${t.message}")
                t.printStackTrace()
                // Add any additional error handling code here
                Toast.makeText(
                    this@FilterActivity,
                    "API Call Failed: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })


    }


}