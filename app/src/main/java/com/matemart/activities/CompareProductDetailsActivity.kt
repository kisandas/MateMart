package com.matemart.activities


import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.adapter.ItemVariationAdapterForCompareProduct
import com.matemart.databinding.ActivityCompareProductDetailsBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.interfaces.SliderItemClickListner
import com.matemart.model.AddCartResponse
import com.matemart.model.CompareProductDetailResponse
import com.matemart.model.CompareVariation
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

        binding.btnAddToCart.setOnClickListener { addToCartItem() }


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
                            this@CompareProductDetailsActivity, "Item Added to Cart", Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@CompareProductDetailsActivity, CartActivity::class.java))
                    } else {
                        Toast.makeText(
                            this@CompareProductDetailsActivity, "Something went wrong", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                if (isLastIteration) {
                    Toast.makeText(
                        this@CompareProductDetailsActivity, "Something went wrong", Toast.LENGTH_LONG
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
                            data.variation,this@CompareProductDetailsActivity
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


                        readMoreOption.addReadMoreTo(
                            binding.tvDescription,
                            Html.fromHtml(description)
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


    override fun ItemClick(cardPosition: Int, position: Int) {


    }

    override fun onItemAddQTY(variation: CompareVariation) {
        val item = selectedVariation.find { it.productDetailId == variation.productDetailId }
        if(item != null) {
            val index = selectedVariation.indexOf(item)
           if(index != -1){
               selectedVariation[index] = variation
           }
        }else{
            selectedVariation.add(variation)
        }


        calculateAndSetValue(selectedVariation)


    }

    override fun onItemRemoveQTY(variation: CompareVariation) {
        val item = selectedVariation.find { it.productDetailId == variation.productDetailId }
        if(item != null) {
            val index = selectedVariation.indexOf(item)
            if(index != -1){
                selectedVariation[index] = variation
            }
        }else{
            selectedVariation.removeIf{it.productDetailId == variation.productDetailId}
        }

        calculateAndSetValue(selectedVariation)
    }

    fun calculateAndSetValue(selectedVariation: ArrayList<CompareVariation>){
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

}
