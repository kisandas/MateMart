package com.matemart.activities


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.adapter.ItemCompareBrandAdapter
import com.matemart.adapter.ItemVariationAdapterForCompareProduct
import com.matemart.databinding.ActivityCompareProductDetailsBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.interfaces.SliderItemClickListner
import com.matemart.model.*
import com.matemart.utils.ReadMoreOption
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompareProductDetailsActivity : AppCompatActivity(), SliderItemClickListner,
    ProductVariationClickItem {
    var p_id: Int = 0
    var product_detail_id: Int = 0

    lateinit var adapter: ItemVariationAdapterForCompareProduct
    lateinit var brandAdapter: ItemCompareBrandAdapter

    var selectedVariation: ArrayList<CompareVariation> = arrayListOf()

    lateinit var binding: ActivityCompareProductDetailsBinding;
    lateinit var readMoreOption: ReadMoreOption


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompareProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        readMoreOption = ReadMoreOption.Builder(this)
            .textLength(2, ReadMoreOption.TYPE_LINE)
            .moreLabel(getString(R.string.read_more))
            .lessLabel(getString(R.string.read_less))
            .moreLabelColor(ContextCompat.getColor(this, R.color.dark_gray_4d4d4d))
            .lessLabelColor(ContextCompat.getColor(this, R.color.dark_gray_4d4d4d))
            .labelUnderLine(false)
            .expandAnimation(true)
            .build()

        p_id = intent.getIntExtra("p_id", 0)
        product_detail_id = intent.getIntExtra("product_detail_id", 0)

        getProductDetail(p_id, product_detail_id)

        binding.rlCart.setOnClickListener {
            startActivity(Intent(this@CompareProductDetailsActivity,CartActivity::class.java))
        }

        binding.btnAddToCart.setOnClickListener { addToCartItem() }
        binding.llCompareHeader.visibility = GONE

        binding.icCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                getCompareBrand()
            } else {

                binding.llCompareHeader.visibility = GONE
                binding.rcCompareBrand.visibility = GONE

            }
        }





    }


    private fun addToCartItem() {
        if (selectedVariation.size > 0) {
            for ((index, item) in selectedVariation.withIndex()) {

                val isLastIteration = index == selectedVariation.size - 1
                item.productDetailId?.let {
                    addToCart(
                        it, item.currentQty, isLastIteration
                    )
                }
            }
        }
    }

    private fun addToCart(productDetailId: Int, count: Int, isLastIteration: Boolean) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", productDetailId)
        jsonObject.addProperty("qty", count)
        jsonObject.addProperty("sample", 0)

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@CompareProductDetailsActivity)
        var call: Call<AddCartResponse> = apiInterface.addIntoCart(jsonObject)!!

        call.enqueue(object : Callback<AddCartResponse> {
            override fun onResponse(
                call: Call<AddCartResponse>, response: Response<AddCartResponse>
            ) {
                if (isLastIteration) {

                    if (response.body()?.statuscode == 11) {
                        Toast.makeText(
                            this@CompareProductDetailsActivity,
                            "Item Added to Cart",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(
                            Intent(
                                this@CompareProductDetailsActivity,
                                CartActivity::class.java
                            )
                        )
                        finish()
                    } else {
                        Toast.makeText(
                            this@CompareProductDetailsActivity,
                            "Something went wrong",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                if (isLastIteration) {
                    Toast.makeText(
                        this@CompareProductDetailsActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        })

    }


    private fun getProductDetail(p_id: Int, product_detail_id: Int) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", product_detail_id)
        jsonObject.addProperty("p_id", p_id)

        val apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        val call: Call<CompareProductDetailResponse> =
            apiInterface.getCompareProductDetail(jsonObject)!!

        call.enqueue(object : Callback<CompareProductDetailResponse> {
            override fun onResponse(
                call: Call<CompareProductDetailResponse>,
                response: Response<CompareProductDetailResponse>
            ) {
                if (response.body()?.statuscode == 11) {

                    val data = response.body()?.data

                    if (data != null) {

                        binding.tvProductName.text = data.product?.pName
                        binding.tvRating.text = "${data.rating}"


                        adapter = ItemVariationAdapterForCompareProduct(
                            this@CompareProductDetailsActivity,
                            data.variation, this@CompareProductDetailsActivity
                        )
                        binding.rcVariation.layoutManager =
                            LinearLayoutManager(this@CompareProductDetailsActivity, VERTICAL, false)
                        binding.rcVariation.adapter = adapter


                        val imageList = ArrayList<SlideModel>()
                        if (data.product?.image != null) {
                            for (item in data.product!!.image) {
                                imageList.add(
                                    SlideModel(
                                        item,
                                        ScaleTypes.FIT
                                    )
                                )
                            }
                        }

                        binding.imageSlider.setImageList(
                            0,
                            imageList,
                            ScaleTypes.CENTER_INSIDE,
                            this@CompareProductDetailsActivity
                        )


                        val description = data.product?.description
                        val detailDescription = data.product?.detail_desc+"\n"

                        readMoreOption.addReadMoreTo(
                            binding.tvDescription,
                            Html.fromHtml(description)
                        )
                        readMoreOption.addReadMoreTo(
                            binding.tvDetailDescription, Html.fromHtml(detailDescription)
                        )

                    }

                } else {
                    Toast.makeText(
                        this@CompareProductDetailsActivity,
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CompareProductDetailResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@CompareProductDetailsActivity,
                    t.toString(),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })
    }

    private fun getCompareBrand() {
        val jsonObject = JsonObject()

        for (item in selectedVariation) {
//            jsonObject.addProperty(item.productDetailId.toString(),item.currentQty)
            jsonObject.addProperty("17644",item.currentQty)
        }

//        jsonObject.addProperty("17645",5)
//        jsonObject.addProperty("17644",5)
//        jsonObject.addProperty("17646",5)

        val json = JsonObject()

        json.add("product_info",jsonObject)

        Log.e("mmmmmm", "getCompareBrand: "+json )




        val apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        val call: Call<CompareBrandFilter> =
            apiInterface.getCompareBrand(json)!!

        call.enqueue(object : Callback<CompareBrandFilter> {
            override fun onResponse(
                call: Call<CompareBrandFilter>,
                response: Response<CompareBrandFilter>
            ) {
                if (response.body()?.statuscode == 11) {

                    val data = response.body()?.data

                    if (data != null) {
                        binding.llCompareHeader.visibility = VISIBLE
                        binding.rcCompareBrand.visibility = VISIBLE

                        val listBrand = data.compareResult

                        brandAdapter =
                            ItemCompareBrandAdapter(this@CompareProductDetailsActivity, listBrand,this@CompareProductDetailsActivity)

                        binding.rcCompareBrand.layoutManager =
                            LinearLayoutManager(this@CompareProductDetailsActivity, VERTICAL, false)
                        binding.rcCompareBrand.adapter = brandAdapter

                    }

                } else {
                    Toast.makeText(
                        this@CompareProductDetailsActivity,
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CompareBrandFilter>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@CompareProductDetailsActivity,
                    t.toString(),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })
    }

    override fun ItemClick(cardPosition: Int, position: Int) {


    }

    override fun onItemAddQTY(variation: CompareVariation) {
        val item = selectedVariation.find { it.productDetailId == variation.productDetailId }
        if (item != null) {
            val index = selectedVariation.indexOf(item)
            if (index != -1) {
                selectedVariation[index] = variation
            }
        } else {
            selectedVariation.add(variation)
        }


        calculateAndSetValue(selectedVariation)


    }

    override fun onItemRemoveQTY(variation: CompareVariation) {
        val item = selectedVariation.find { it.productDetailId == variation.productDetailId }
        if (item != null) {
            val index = selectedVariation.indexOf(item)
            if (index != -1) {
                selectedVariation[index] = variation
            }
        } else {
            selectedVariation.removeIf { it.productDetailId == variation.productDetailId }
        }

        calculateAndSetValue(selectedVariation)
    }

    override fun onAddItemIntoCart(position: Int,data:CompareResult) {

        if (data.addtocartdata.size > 0) {
            for ((index, item) in data.addtocartdata.withIndex()) {

                val isLastIteration = index == data.addtocartdata.size - 1
                item.productDetailId?.let {
                    item.totalQty?.toInt()?.let { it1 ->
                        addToCart(
                            it, it1, isLastIteration
                        )
                    }
                }
            }
        }

    }

    fun calculateAndSetValue(selectedVariation: ArrayList<CompareVariation>) {
        var totalQTY = 0
        var totalAmount = 0.0

        for (variation in selectedVariation) {
            // Add the currentQty to the totalQuantity
            totalQTY += variation.currentQty

            // Convert the saleprice to a numeric value (assuming it's a valid number)
            val salePriceValue = variation.saleprice?.toDoubleOrNull() ?: 0.0

            // Calculate the amount for this variation and add it to the totalAmount
            val variationAmount = variation.currentQty * salePriceValue
            totalAmount += variationAmount
        }

        binding.tvTotalQTYValue.text = "$totalQTY"
        binding.tvTotalPriceValue.text = String.format("%.2f", totalAmount)


    }




}

interface ProductVariationClickItem {
    fun onItemAddQTY(variation: CompareVariation)

    fun onItemRemoveQTY(variation: CompareVariation)

    fun onAddItemIntoCart(position: Int,data: CompareResult)

}


