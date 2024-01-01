package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.example.example.RemoveCartResponse
import com.google.gson.JsonObject
import com.matemart.R
import com.matemart.adapter.ImagePreviewSliderAdapter
import com.matemart.adapter.RatingBarListAdapter
import com.matemart.adapter.ReviewListAdapter
import com.matemart.adapter.VariationOuterAdapter
import com.matemart.databinding.ActivityProductDetailsBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.interfaces.SliderItemClickListner
import com.matemart.model.AddCartResponse
import com.matemart.model.GetProductDetailsResponse
import com.matemart.model.Product
import com.matemart.model.ViewListModel
import com.matemart.utils.*
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

    val count = intArrayOf(0)

    var product: Product? = null

    companion object {
        val finalSelectedVariation = HashMap<String, String>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        readMoreOption = ReadMoreOption.Builder(this)
            .textLength(3, ReadMoreOption.TYPE_LINE) // OR
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
        var badgeCount = "0"
        if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.BADGE_COUNT, 0)!! > 0 && SharedPrefHelper.getInstance(
                MyApplication.getInstance()
            )
                .read(SharedPrefHelper.BADGE_COUNT, 0)!! > 99
        ) {
            badgeCount = "99+"

        } else if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.BADGE_COUNT, 0)!! > 0
        ) {
            badgeCount = SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.BADGE_COUNT, 0).toString()
        }


        binding.tvCartCount.text = badgeCount

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.rlCart.setOnClickListener {
            startActivity(Intent(this@ProductDetailsActivity, CartActivity::class.java))
        }

        binding.ivPlus.setOnClickListener {

            if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                    .read(SharedPrefHelper.IS_USER_GUEST, false)
            ) {
                Toast.makeText(
                    this@ProductDetailsActivity,
                    Utils.LOGIN_MESSAGE,
                    Toast.LENGTH_LONG
                ).show()

                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

                pref.write(SharedPrefHelper.ADDRESS_ID, 0)
                pref.write(SharedPrefHelper.EMAIL, "")
                pref.write(SharedPrefHelper.USER_ID, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_NUMBER, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_TOKEN, "")
                pref.write(SharedPrefHelper.KEY_CCID, "")
                pref.write(SharedPrefHelper.KEY_LOGGED_IN, false)


                startActivity(Intent(this@ProductDetailsActivity, LoginActivity::class.java))

            } else {
                product?.let {
                    if (count[0] < product?.total_quantity!!) {
                        count[0]++
                        binding.tvTotalAmount.text = "₹ " + (count[0] * it.saleprice.toDouble())
                        if (count[0] > 0) {
                            binding.ivMinus.visibility = View.VISIBLE
                            binding.tvCount.visibility = View.VISIBLE
                            binding.tvCount.text = "" + count[0]
//                            addToCart(product!!, count[0])
                            //                    call ApI for Add into cart
                        }
                    } else {
                        Toast.makeText(
                            this@ProductDetailsActivity,
                            "Only " + product!!.total_quantity + " items available",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.ivMinus.setOnClickListener {
            count[0]--
            product?.let {

                binding.tvTotalAmount.text = "₹ " + (count[0] * it.saleprice.toDouble())
                if (count[0] < 1) {
                    binding.ivMinus.visibility = View.GONE
                    binding.tvCount.visibility = View.GONE
//                    removeFromCart(product!!)
//                    call ApI for Remove from cart
                } else {
//                    addToCart(product!!, count[0])
                }

                binding.tvCount.text = "" + count[0]
            }
        }

        binding.llAddToCart.setOnClickListener {
            product?.let { it1 -> addToCart(it1, count[0]) }
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
                        binding.tvPrice.text = data.product.price
                        binding.tvSalePrice.text = data.product.saleprice
                        product = data.product

                        if (data.product.cart != null) {
                            count[0] = data.product.cart.qty
                            binding.tvCount.text = data.product.cart.qty.toString()

                            if (count[0] <= 0) {
                                binding.ivPlus.visibility = VISIBLE
                                binding.ivMinus.visibility = GONE
                                binding.tvCount.visibility = GONE

                            } else {
                                binding.ivPlus.visibility = VISIBLE
                                binding.ivMinus.visibility = VISIBLE
                                binding.tvCount.visibility = VISIBLE
                            }
                        } else {
                            binding.ivPlus.visibility = VISIBLE
                            binding.ivMinus.visibility = GONE
                            binding.tvCount.visibility = GONE
                        }

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

                        binding.imageSlider.setItemClickListener(object : SliderItemClickListner {
                            override fun ItemClick(cardPosition: Int, position: Int) {

                                val intent = Intent(
                                    this@ProductDetailsActivity,
                                    ImagePreviewActivity::class.java
                                )
                                intent.putStringArrayListExtra(
                                    "imageUrl",
                                    ArrayList(data.product.images)
                                )
                                intent.putExtra("position", position)
                                startActivity(intent)
                            }
                        })


//                        binding.rcImageList.apply {
//                            val adapter = ImagePreviewSliderAdapter(
//                                this@ProductDetailsActivity,
//                                data.product.images,
//                                this@ProductDetailsActivity
//                            )
//                            layoutManager =
//                                LinearLayoutManager(this@ProductDetailsActivity, HORIZONTAL, false)
//                            binding.rcImageList.adapter = adapter
//
//                            var obj = object : ItemChangeListener {
//                                override fun onItemChanged(position: Int) {
//                                    adapter.setSelectedPosition(position)
//                                }
//                            }
//                        }

                        if (data.variation.isEmpty()) {
                            binding.rcVariationMain.visibility = GONE
                        } else if (data.variation.containsKey("No Variation")) {
                            binding.rcVariationMain.visibility = GONE
                        } else {
                            binding.rcVariationMain.visibility = VISIBLE
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

                        }


                        val description = data.product.description + "\n"
                        val detail_description = data.product.detail_desc + "\n"

                        readMoreOption.addReadMoreTo(
                            binding.tvDescription, Html.fromHtml(description)
                        )

                        readMoreOption.addReadMoreTo(
                            binding.tvDetailDescription, Html.fromHtml(detail_description)
                        )

                        adapterReview = ReviewListAdapter(this@ProductDetailsActivity, data.review)
                        binding.rcReviews.layoutManager = LinearLayoutManager(
                            this@ProductDetailsActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                        binding.rcReviews.adapter = adapterReview

                        if (data.review.isEmpty()) {
                            binding.rcReviews.visibility = GONE
                            binding.tvAllReview.visibility = GONE
                            binding.viewReview.visibility = GONE
                            binding.llReview.visibility = GONE
                        } else {
                            binding.rcReviews.visibility = VISIBLE
                            binding.tvAllReview.visibility = VISIBLE
                            binding.viewReview.visibility = VISIBLE
                            binding.llReview.visibility = VISIBLE
                        }

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

                        binding.tvTitle.text = data.product.p_name
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

    private fun addToCart(item: Product, count: Int) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", item.product_detail_id)
        jsonObject.addProperty("qty", count)
        jsonObject.addProperty("sample", 0)

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@ProductDetailsActivity)
        var call: Call<AddCartResponse> = apiInterface.addIntoCart(jsonObject)!!

        call.enqueue(object : Callback<AddCartResponse> {
            override fun onResponse(
                call: Call<AddCartResponse>,
                response: Response<AddCartResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    Toast.makeText(
                        this@ProductDetailsActivity,
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity,
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                Toast.makeText(
                    this@ProductDetailsActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }

    private fun removeFromCart(item: Product) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", item.product_detail_id)


        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@ProductDetailsActivity)
        var call: Call<RemoveCartResponse> = apiInterface.removeFromCart(jsonObject)!!

        call.enqueue(object : Callback<RemoveCartResponse> {
            override fun onResponse(
                call: Call<RemoveCartResponse>,
                response: Response<RemoveCartResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    Toast.makeText(
                        this@ProductDetailsActivity,
                        "Item Removed from Cart",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<RemoveCartResponse>, t: Throwable) {
                Toast.makeText(
                    this@ProductDetailsActivity,
                    "Something went wrong",
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
                        binding.tvPrice.text = data.product.price
                        binding.tvSalePrice.text = data.product.saleprice

                        product = data.product

                        if (data.product.cart != null) {
                            count[0] = data.product.cart.qty
                        }

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


                        val description = data.product.description + "\n"
                        val detailDescription = data.product.detail_desc + "\n"

                        Log.e("ccccccccc", "onResponse: " + description)

                        readMoreOption.addReadMoreTo(
                            binding.tvDescription, Html.fromHtml(description)
                        )

                        readMoreOption.addReadMoreTo(
                            binding.tvDetailDescription, Html.fromHtml(detailDescription)
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


    }


}

interface onVariationChangeListener {
    fun onVariationChanged(variation: HashMap<String, String>)
}