package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.adapter.ImagePreviewSliderAdapter
import com.matemart.adapter.RatingBarListAdapter
import com.matemart.adapter.ReviewListAdapter
import com.matemart.adapter.VariationOuterAdapter
import com.matemart.databinding.ActivityProductDetailsBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.interfaces.SliderItemClickListner
import com.matemart.model.GetProductDetailsResponse
import com.matemart.utils.ReadMoreOption
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity(), SliderItemClickListner,
    onVariationChangeListener, ImagePreviewSliderAdapter.OnItemClickListener {
    var p_id: Int = 0
    var product_detail_id: Int = 0
    private var tableRowHeader: TableRow? = null
    private var tableLayout: TableLayout? = null

    lateinit var binding: ActivityProductDetailsBinding;
    lateinit var readMoreOption: ReadMoreOption
    var adapterReview: ReviewListAdapter? = null
    var adapterRating: RatingBarListAdapter? = null
    var adapterVariationOuter: VariationOuterAdapter? = null

    companion object {
        val finalSelectedVariation = HashMap<String, String>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        readMoreOption = ReadMoreOption.Builder(this)
            .textLength(2, ReadMoreOption.TYPE_LINE) // OR
            //.textLength(300, ReadMoreOption.TYPE_CHARACTER)
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

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.tvAllReview.setOnClickListener {
            startActivity(
                Intent(
                    this@ProductDetailsActivity,
                    ReviewListActivity::class.java
                ).putExtra("p_id", p_id)
            )
        }

    }


    private fun getProductDetail(p_id: Int, product_detail_id: Int) {
        var jsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", product_detail_id)
        jsonObject.addProperty("p_id", p_id)

        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<GetProductDetailsResponse> = apiInterface.getProductDetail(jsonObject)!!

        call.enqueue(object : Callback<GetProductDetailsResponse> {
            override fun onResponse(
                call: Call<GetProductDetailsResponse>,
                response: Response<GetProductDetailsResponse>
            ) {
                if (response.body()?.statusCode == 11) {

                    val data = response.body()?.data

                    if (data != null) {

                        binding.tvProductName.text = data.product.p_name
                        binding.tvPrice.text = data.product.saleprice
                        binding.tvSalePrice.text = data.product.price

                        val imageList = ArrayList<SlideModel>()
                        for (item in data.product.images) {
                            imageList.add(
                                SlideModel(
                                    item,
                                    ScaleTypes.FIT
                                )
                            )
                        }

                        binding.imageSlider.setImageList(
                            0,
                            imageList,
                            ScaleTypes.CENTER_INSIDE,
                            this@ProductDetailsActivity
                        )


                        binding.rcImageList.apply {
                            val adapter = ImagePreviewSliderAdapter(
                                this@ProductDetailsActivity,
                                data.product.images,
                                this@ProductDetailsActivity
                            )
                            layoutManager =
                                LinearLayoutManager(this@ProductDetailsActivity, HORIZONTAL, false)
                            binding.rcImageList.adapter = adapter

                            var obj = object : ItemChangeListener {
                                override fun onItemChanged(position: Int) {
                                    adapter.setSelectedPosition(position)
                                }
                            }
                        }


                        adapterVariationOuter = VariationOuterAdapter(
                            this@ProductDetailsActivity,
                            data.variation,
                            data.filtervariation_data,
                            data.variation_data.variations,
                            this@ProductDetailsActivity
                        )

                        binding.rcVariationMain.layoutManager = LinearLayoutManager(
                            this@ProductDetailsActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                        binding.rcVariationMain.adapter = adapterVariationOuter


                        val description = data.product.description


                        readMoreOption.addReadMoreTo(
                            binding.tvDescription, Html.fromHtml(description)
                        )


                        adapterReview = ReviewListAdapter(this@ProductDetailsActivity, data.review)
                        binding.rcReviews.layoutManager = LinearLayoutManager(
                            this@ProductDetailsActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                        binding.rcReviews.adapter = adapterReview

                        adapterRating = RatingBarListAdapter(
                            this@ProductDetailsActivity,
                            data.ratings,
                            data.rating
                        )
                        binding.rcRating.layoutManager = LinearLayoutManager(
                            this@ProductDetailsActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                        binding.rcRating.adapter = adapterRating


                        binding.tvTotalRating.text =
                            "${data.rating}  Ratings And ${data.review_total} Reviews"

                    }


                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity,
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<GetProductDetailsResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("lllllllllll", "onFailure: " + t.message)
                Toast.makeText(
                    this@ProductDetailsActivity,
                    t.toString(),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })
    }


    private fun getFilteredProductDetail(
        p_id: Int,
        product_detail_id: Int,
        variation: HashMap<String, String>
    ) {
        var jsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", product_detail_id)
        jsonObject.addProperty("p_id", p_id)


        val variationJsonObject = JsonObject()
        for ((key, value) in variation) {
            variationJsonObject.addProperty(key, value)
        }

        jsonObject.add("variation", variationJsonObject)

        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<GetProductDetailsResponse> =
            apiInterface.getSingleProductDetail(jsonObject)!!

        call.enqueue(object : Callback<GetProductDetailsResponse> {
            override fun onResponse(
                call: Call<GetProductDetailsResponse>,
                response: Response<GetProductDetailsResponse>
            ) {
                if (response.body()?.statusCode == 11) {

                    val data = response.body()?.data

                    if (data != null) {

                        binding.tvProductName.text = data.product.p_name
                        binding.tvPrice.text = data.product.saleprice
                        binding.tvSalePrice.text = data.product.price

                        val imageList = ArrayList<SlideModel>()
                        for (item in data.product.images) {
                            imageList.add(
                                SlideModel(
                                    item,
                                    ScaleTypes.FIT
                                )
                            )
                        }

                        binding.imageSlider.setImageList(
                            0,
                            imageList,
                            ScaleTypes.CENTER_INSIDE,
                            this@ProductDetailsActivity
                        )


                        adapterVariationOuter = VariationOuterAdapter(
                            this@ProductDetailsActivity,
                            data.variation,
                            data.filtervariation_data,
                            data.variation_data.variations,
                            this@ProductDetailsActivity
                        )
                        binding.rcVariationMain.layoutManager = LinearLayoutManager(
                            this@ProductDetailsActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                        binding.rcVariationMain.adapter = adapterVariationOuter


                        val description = data.product.description

                        Log.e("ccccccccc", "onResponse: " + description)

                        readMoreOption.addReadMoreTo(
                            binding.tvDescription, Html.fromHtml(description)
                        )


                        adapterReview = ReviewListAdapter(this@ProductDetailsActivity, data.review)
                        binding.rcReviews.layoutManager = LinearLayoutManager(
                            this@ProductDetailsActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                        binding.rcReviews.adapter = adapterReview

                        adapterRating = RatingBarListAdapter(
                            this@ProductDetailsActivity,
                            data.ratings,
                            data.rating
                        )
                        binding.rcRating.layoutManager = LinearLayoutManager(
                            this@ProductDetailsActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                        binding.rcRating.adapter = adapterRating


                        binding.tvTotalRating.text =
                            "${data.rating}  Ratings And ${data.review_total} Reviews"

                    }
                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity,
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<GetProductDetailsResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@ProductDetailsActivity,
                    t.toString(),
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }


    override fun ItemClick(cardPosition: Int, position: Int) {
    }

    override fun onVariationChanged(variation: HashMap<String, String>) {
        Log.e("mmmmmmmmmmmmm", "onVariationChanged: " + variation.toString())
        getFilteredProductDetail(p_id, product_detail_id, variation)
    }

    override fun onItemClick(position: Int, itemList: List<String>) {

        val intent = Intent(this@ProductDetailsActivity, ImagePreviewActivity::class.java)
        intent.putStringArrayListExtra("imageUrl",  ArrayList(itemList))
        intent.putExtra("position",position)
        startActivity(intent)

    }


}

interface onVariationChangeListener {
    fun onVariationChanged(variation: HashMap<String, String>)
}