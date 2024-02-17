package matemart.material.Interior.activities

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.example.RemoveCartResponse
import com.google.gson.JsonObject
import matemart.material.Interior.R
import matemart.material.Interior.adapter.*
import matemart.material.Interior.databinding.ActivityProductDetailsBinding
import matemart.material.Interior.interfaces.ApiInterface
import matemart.material.Interior.model.*
import matemart.material.Interior.utils.MyApplication
import matemart.material.Interior.utils.Service
import matemart.material.Interior.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsActivity : AppCompatActivity(),
    matemart.material.Interior.interfaces.SliderItemClickListner,
    onVariationChangeListener, ImagePreviewSliderAdapter.OnItemClickListener,
    matemart.material.Interior.interfaces.WishListUpdateListner {
    var p_id: Int = 0
    var product_detail_id: Int = 0
    private var tableRowHeader: TableRow? = null
    private var tableLayout: TableLayout? = null

    lateinit var binding: ActivityProductDetailsBinding;
    lateinit var readMoreOption: matemart.material.Interior.utils.ReadMoreOption
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


        readMoreOption = matemart.material.Interior.utils.ReadMoreOption.Builder(this)
            .textLength(3, matemart.material.Interior.utils.ReadMoreOption.TYPE_LINE) // OR
            //.textLength(300, ReadMoreOption.TYPE_CHARACTER)
            .moreLabel(getString(R.string.read_more)).lessLabel(getString(R.string.read_less))
            .moreLabelColor(ContextCompat.getColor(this, R.color.dark_gray_4d4d4d))
            .lessLabelColor(ContextCompat.getColor(this, R.color.dark_gray_4d4d4d))
            .labelUnderLine(false).expandAnimation(true).build()

        p_id = intent.getIntExtra("p_id", 0)
        product_detail_id = intent.getIntExtra("product_detail_id", 0)

        getProductDetail(p_id, product_detail_id)
        setBadgeOnCart()

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
                    matemart.material.Interior.utils.Utils.LOGIN_MESSAGE,
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
            product?.let { it1 ->
                if (count[0] > 0) {
                    addToCart(it1, count[0])
                } else {
                    matemart.material.Interior.utils.Toast.Toaster.Builder(this@ProductDetailsActivity)
                        .setTitle("ERROR")
                        .setDescription("Select Quantity to Add into Cart").setDuration(5000)
                        .setStatus(matemart.material.Interior.utils.Toast.Toaster.Status.ERROR)
                        .show()
                }
            }
        }

        binding.ivShare.setOnClickListener {

            if(product?.url?.isNotEmpty() == true) {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                share.putExtra(Intent.EXTRA_TEXT, product?.url)
                startActivity(Intent.createChooser(share, "Share link!"))
            }
        }


        binding.tvAllReview.setOnClickListener {
            startActivity(
                Intent(
                    this@ProductDetailsActivity, ReviewListActivity::class.java
                ).putExtra("p_id", p_id)
            )
        }


        binding.ivLikeUnlikeProduct.setOnClickListener {

            if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                    .read(SharedPrefHelper.IS_USER_GUEST, false)
            ) {
                Toast.makeText(
                    this@ProductDetailsActivity,
                    matemart.material.Interior.utils.Utils.LOGIN_MESSAGE,
                    Toast.LENGTH_LONG
                ).show()

                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())
                pref.logoutProfile(this@ProductDetailsActivity)
            } else {
                updateWishList(p_id, 1)
            }
        }

        binding.ivRemoveWhishlist.setOnClickListener {
            if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                    .read(SharedPrefHelper.IS_USER_GUEST, false)
            ) {
                Toast.makeText(
                    this@ProductDetailsActivity,
                    matemart.material.Interior.utils.Utils.LOGIN_MESSAGE,
                    Toast.LENGTH_LONG
                ).show()

                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

                pref.logoutProfile(this@ProductDetailsActivity)

            } else {
                updateWishList(p_id, 0)
            }
        }


    }


    fun updateWishList(productId: Int, is_wishlist: Int) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("p_id", productId)
        jsonObject.addProperty("is_wishlist", is_wishlist)
        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@ProductDetailsActivity)

        var call: Call<JsonObject> = apiInterface.updateWishList(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    if (is_wishlist == 0) {
                        binding.ivLikeUnlikeProduct.visibility = VISIBLE
                        binding.ivRemoveWhishlist.visibility = GONE
                    } else {
                        binding.ivLikeUnlikeProduct.visibility = GONE
                        binding.ivRemoveWhishlist.visibility = VISIBLE
                    }

//                    updateListner.onUpdate()
                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity,
                        "Something went Wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    this@ProductDetailsActivity,
                    "Something went Wrong",
                    Toast.LENGTH_SHORT
                ).show()

            }
        })


    }


    private fun getProductDetail(p_id: Int, product_detail_id: Int) {
        var jsonObject = JsonObject()
        jsonObject.addProperty("product_detail_id", product_detail_id)
        jsonObject.addProperty("p_id", p_id)

        getSimilarProduct(p_id)

        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<GetProductDetailsResponse> = apiInterface.getProductDetail(jsonObject)!!

        call.enqueue(object : Callback<GetProductDetailsResponse> {
            override fun onResponse(
                call: Call<GetProductDetailsResponse>, response: Response<GetProductDetailsResponse>
            ) {
                if (response.body()?.statusCode == 11) {

                    val data = response.body()?.data

                    if (data != null) {

                        binding.tvProductName.text = data.product.p_name
                        binding.tvPrice.text = data.product.price
//                        binding.tvSalePrice.text = data.product.saleprice

//                        var discountPrice = data.product.price.toDouble() - data.product.saleprice.toDouble()
//                        var discountPercentage = (discountPrice/data.product.price.toDouble())*100


                        if (data.product.is_wishlist == 0) {
                            binding.ivLikeUnlikeProduct.visibility = VISIBLE
                            binding.ivRemoveWhishlist.visibility = GONE
                        } else {
                            binding.ivLikeUnlikeProduct.visibility = GONE
                            binding.ivRemoveWhishlist.visibility = VISIBLE
                        }

                        if (data.product.discount != 0) {
                            val mainPrice = (data.product.price ?: "0").toDoubleOrNull() ?: 0.0
                            val salePrice = (data.product.saleprice ?: "0").toDoubleOrNull() ?: 0.0
                            val discountPrice = mainPrice - salePrice
                            var discountPercentage = (discountPrice / mainPrice) * 100
                            discountPercentage += data.product.discount

                            val lastPrice = salePrice - (salePrice * data.product.discount / 100)
                            binding.tvSalePrice.text = String.format("₹%.2f", lastPrice)

                            binding.tvPercentageOff.text = "${Math.round(discountPercentage)}% OFF"
                            binding.tvPercentageOff.visibility = VISIBLE
                        } else {
                            Log.e("mmmmmmmmmmm", "onBindViewHolder: " + data.product.saleprice)
                            binding.tvPercentageOff.visibility = GONE
                            binding.tvSalePrice.text =
                                String.format("₹%.2f", data.product.saleprice.toDouble() ?: 0)
                        }


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
                                    item, ScaleTypes.FIT
                                )
                            )
                        }

                        binding.imageSlider.setImageList(
                            0, imageList, ScaleTypes.CENTER_INSIDE, this@ProductDetailsActivity
                        )

                        binding.imageSlider.setItemClickListener(object :
                            matemart.material.Interior.interfaces.SliderItemClickListner {
                            override fun ItemClick(cardPosition: Int, position: Int) {

                                val intent = Intent(
                                    this@ProductDetailsActivity, ImagePreviewActivity::class.java
                                )
                                intent.putStringArrayListExtra(
                                    "imageUrl", ArrayList(data.product.images)
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
                                this@ProductDetailsActivity, RecyclerView.VERTICAL, false
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
                            this@ProductDetailsActivity, RecyclerView.VERTICAL, false
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
                            this@ProductDetailsActivity, data.ratings, data.rating
                        )
                        binding.rcRating.layoutManager = LinearLayoutManager(
                            this@ProductDetailsActivity, RecyclerView.VERTICAL, false
                        )
                        binding.rcRating.adapter = adapterRating

                        binding.tvTitle.text = data.product.p_name
                        binding.tvTotalRating.text =
                            "${data.rating}  Ratings And\n ${data.review_total} Reviews"

                    }


                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity, response.body()?.message, Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<GetProductDetailsResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("lllllllllll", "onFailure: " + t.message)
                Toast.makeText(
                    this@ProductDetailsActivity, t.toString(), Toast.LENGTH_LONG
                ).show()
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
                call: Call<AddCartResponse>, response: Response<AddCartResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    getUserProfile()
                    Toast.makeText(
                        this@ProductDetailsActivity, response.body()?.message, Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity, response.body()?.message, Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                Toast.makeText(
                    this@ProductDetailsActivity, "Something went wrong", Toast.LENGTH_LONG
                ).show()
            }

        })

    }

    private fun setBadgeOnCart() {
        var badgeCount = "0"
        if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.BADGE_COUNT, 0)!! > 0 && SharedPrefHelper.getInstance(
                MyApplication.getInstance()
            ).read(SharedPrefHelper.BADGE_COUNT, 0)!! > 99
        ) {
            binding.tvCartCount.visibility = VISIBLE
            badgeCount = "99+"

        } else if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.BADGE_COUNT, 0)!! > 0
        ) {
            binding.tvCartCount.visibility = VISIBLE
            badgeCount = SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.BADGE_COUNT, 0).toString()
        } else {
            binding.tvCartCount.visibility = GONE
        }



        binding.tvCartCount.text = badgeCount
    }

    private fun getUserProfile() {
        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@ProductDetailsActivity)
        var call: Call<ResGetProfileDetails> = apiInterface.getUserProfile()!!

        call.enqueue(object : Callback<ResGetProfileDetails> {
            override fun onResponse(
                call: Call<ResGetProfileDetails>, response: Response<ResGetProfileDetails>
            ) {

                if (response.isSuccessful) {
                    if (response.body()?.statuscode == 11) {
                        response.body()?.data?.let {
                            if (it.cart_badge_count !== null && it.cart_badge_count!! > 0) {
                                SharedPrefHelper.getInstance(MyApplication.getInstance())
                                    .write(SharedPrefHelper.BADGE_COUNT, it.cart_badge_count!!)
                            } else {
                                SharedPrefHelper.getInstance(MyApplication.getInstance())
                                    .write(SharedPrefHelper.BADGE_COUNT, 0)
                            }

                            setBadgeOnCart()
                        }
                    }


                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity, "Something went wrong", Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetProfileDetails>, t: Throwable) {
                Log.e("jjjjjjjjjjj", "onFailure: " + t.message)
                Toast.makeText(
                    this@ProductDetailsActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                ).show()
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
                call: Call<RemoveCartResponse>, response: Response<RemoveCartResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    Toast.makeText(
                        this@ProductDetailsActivity, "Item Removed from Cart", Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity, "Something went wrong", Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<RemoveCartResponse>, t: Throwable) {
                Toast.makeText(
                    this@ProductDetailsActivity, "Something went wrong", Toast.LENGTH_LONG
                ).show()
            }

        })

    }


    private fun getFilteredProductDetail(
        p_id: Int, product_detail_id: Int, variation: HashMap<String, String>
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
                call: Call<GetProductDetailsResponse>, response: Response<GetProductDetailsResponse>
            ) {
                if (response.body()?.statusCode == 11) {

                    val data = response.body()?.data

                    if (data != null) {

                        getSimilarProduct(data.product.p_id)

                        binding.tvProductName.text = data.product.p_name
                        binding.tvPrice.text = data.product.price
//                        binding.tvSalePrice.text = data.product.saleprice
//
//                        var discountPrice = data.product.price.toDouble() - data.product.saleprice.toDouble()
//                        var discountPercentage = (discountPrice/data.product.price.toDouble())*100
//                        binding.tvPercentageOff.text = "${Math.round(discountPercentage)}% OFF"

                        if (data.product.discount != 0) {
                            val mainPrice = (data.product.price ?: "0").toDoubleOrNull() ?: 0.0
                            val salePrice = (data.product.saleprice ?: "0").toDoubleOrNull() ?: 0.0
                            val discountPrice = mainPrice - salePrice
                            var discountPercentage = (discountPrice / mainPrice) * 100
                            discountPercentage += data.product.discount

                            val lastPrice = salePrice - (salePrice * data.product.discount / 100)
                            binding.tvSalePrice.text = String.format("₹%.2f", lastPrice)

                            binding.tvPercentageOff.text = "${Math.round(discountPercentage)}% OFF"
                            binding.tvPercentageOff.visibility = VISIBLE
                        } else {
                            Log.e("mmmmmmmmmmm", "onBindViewHolder: " + data.product.saleprice)
                            binding.tvPercentageOff.visibility = GONE
                            binding.tvSalePrice.text =
                                String.format("₹%.2f", data.product.saleprice.toDouble() ?: 0)
                        }


                        product = data.product

                        if (data.product.cart != null) {
                            count[0] = data.product.cart.qty
                        }

                        val imageList = ArrayList<SlideModel>()
                        for (item in data.product.images) {
                            imageList.add(
                                SlideModel(
                                    item, ScaleTypes.FIT
                                )
                            )
                        }

                        binding.imageSlider.setImageList(
                            0, imageList, ScaleTypes.CENTER_INSIDE, this@ProductDetailsActivity
                        )


                        adapterVariationOuter = VariationOuterAdapter(
                            this@ProductDetailsActivity,
                            data.variation,
                            data.filtervariation_data,
                            data.variation_data.variations,
                            this@ProductDetailsActivity
                        )
                        binding.rcVariationMain.layoutManager = LinearLayoutManager(
                            this@ProductDetailsActivity, RecyclerView.VERTICAL, false
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
                            this@ProductDetailsActivity, RecyclerView.VERTICAL, false
                        )
                        binding.rcReviews.adapter = adapterReview

                        adapterRating = RatingBarListAdapter(
                            this@ProductDetailsActivity, data.ratings, data.rating
                        )
                        binding.rcRating.layoutManager = LinearLayoutManager(
                            this@ProductDetailsActivity, RecyclerView.VERTICAL, false
                        )
                        binding.rcRating.adapter = adapterRating


                        binding.tvTotalRating.text =
                            "${data.rating}  Ratings And ${data.review_total} Reviews"

                    }
                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity, response.body()?.message, Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<GetProductDetailsResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@ProductDetailsActivity, t.toString(), Toast.LENGTH_LONG
                ).show()
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


    private fun getSimilarProduct(
        p_id: Int
    ) {
        var jsonObject = JsonObject()
        jsonObject.addProperty("p_id", p_id)


        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResponseProductList> = apiInterface.getSimilarProduct(jsonObject)!!

        call.enqueue(object : Callback<ResponseProductList> {
            override fun onResponse(
                call: Call<ResponseProductList>, response: Response<ResponseProductList>
            ) {
                if (response.body()?.statuscode == 11) {

                    val data = response.body()?.data

                    if (data != null) {
                        val layoutManager = LinearLayoutManager(
                            this@ProductDetailsActivity, LinearLayoutManager.HORIZONTAL, false
                        )
                        binding.rcSimilarProducts.layoutManager = layoutManager
                        val adapter = ProductItemAdapter(
                            data, this@ProductDetailsActivity, this@ProductDetailsActivity
                        )
                        binding.rcSimilarProducts.adapter = adapter

                    }
                } else {
                    Toast.makeText(
                        this@ProductDetailsActivity, response.body()?.message, Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseProductList>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@ProductDetailsActivity, t.toString(), Toast.LENGTH_LONG
                ).show()
            }

        })

    }

    override fun onUpdate() {

    }


}

interface onVariationChangeListener {
    fun onVariationChanged(variation: HashMap<String, String>)
}