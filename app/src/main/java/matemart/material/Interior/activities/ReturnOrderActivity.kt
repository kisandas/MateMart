package matemart.material.Interior.activities

import android.content.res.ColorStateList
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.google.gson.JsonObject
import matemart.material.Interior.R
import matemart.material.Interior.databinding.ActivityReturnOrderBinding
import matemart.material.Interior.interfaces.ApiInterface
import matemart.material.Interior.model.CommonResponse
import matemart.material.Interior.utils.MyApplication
import matemart.material.Interior.utils.Service
import matemart.material.Interior.utils.SharedPrefHelper
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReturnOrderActivity : AppCompatActivity() {
    private var binding: ActivityReturnOrderBinding? = null
    var pref: SharedPrefHelper? = null
    var isIFSC_OK = false
    var o_id = 0
    var o_d_id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReturnOrderBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        pref = SharedPrefHelper.getInstance(MyApplication())

        binding!!.tvCheckIFSC.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        o_id = intent.getIntExtra("o_id", 0)
        o_d_id = intent.getIntExtra("o_d_id", 0)
        validateButton(false)

        binding!!.tvCheckIFSC.setOnClickListener {
            if (binding!!.etIFSCCode.text.toString().trim().length != 11) {
                Toast.makeText(
                    this@ReturnOrderActivity,
                    "Enter Valid IFSC Code",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }



            validateIFSCCODE(binding!!.etIFSCCode.text.toString().trim())

        }

        binding!!.etIFSCCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isIFSC_OK) {
                    isIFSC_OK = false
                }
                validateButton(false)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        binding!!.etAccountNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding!!.etName.text.isNotEmpty() && binding!!.etAccountNumber.text.isNotEmpty() && binding!!.etIFSCCode.text.isNotEmpty()
                    && binding!!.etAccountType.text.isNotEmpty() && isIFSC_OK
                ) {
                    validateButton(true)
                } else {
                    validateButton(false)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        binding!!.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding!!.etName.text.isNotEmpty() && binding!!.etAccountNumber.text.isNotEmpty() && binding!!.etIFSCCode.text.isNotEmpty()
                    && binding!!.etAccountType.text.isNotEmpty() && isIFSC_OK
                ) {
                    validateButton(true)
                } else {
                    validateButton(false)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding!!.btnReturnOrder.setOnClickListener {

            returnOrder(o_id, o_d_id)

        }

    }

    fun validateButton(isEnabled: Boolean) {
        if (isEnabled) {
            val backgroundColor =
                getColor(R.color.theme_blue_38B449) // Replace with your color resource

            val textColor =
                getColor(R.color.white)
            ViewCompat.setBackgroundTintList(
                binding!!.btnReturnOrder,
                ColorStateList.valueOf(backgroundColor)
            )
            binding!!.btnReturnOrder.setTextColor(textColor)
            binding!!.btnReturnOrder.isEnabled = true
            binding!!.btnReturnOrder.isClickable = true
        } else {
            val backgroundColor =
                getColor(R.color.gray_e3e3e3) // Replace with your color resource
            val textColor =
                getColor(R.color.dark_gray)
            ViewCompat.setBackgroundTintList(
                binding!!.btnReturnOrder,
                ColorStateList.valueOf(backgroundColor)
            )
            binding!!.btnReturnOrder.setTextColor(textColor)
            binding!!.btnReturnOrder.isEnabled = false
            binding!!.btnReturnOrder.isClickable = false
        }
    }

    private fun validateIFSCCODE(ifscCode: String) {
        val baseUrl = "https://ifsc.razorpay.com/"

        // Create a Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiInterface::class.java)
        val call = apiService.getIFSCDetails(ifscCode)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    isIFSC_OK = true

                    val responseBody = response.body()
                    if (responseBody != null) {
                        try {
                            val json = responseBody.string()

                            // Parse the JSON response
                            val jsonObject = JSONObject(json)
                            val bankName = jsonObject.getString("BANK")
                            val branchName = jsonObject.getString("BRANCH")
                            binding!!.tvBankName.text = bankName
                            binding!!.tvBranchName.text = branchName
                            binding!!.llBankDetails.visibility = VISIBLE

                            // Now you have the bankName and branchName values
                            // You can use them as needed in your Android app

                        } catch (e: Exception) {
                            e.printStackTrace()
                            binding!!.llBankDetails.visibility = GONE
                            // Handle JSON parsing error
                        }
                    }


                    if (binding!!.etName.text.isNotEmpty() && binding!!.etAccountNumber.text.isNotEmpty() && binding!!.etIFSCCode.text.isNotEmpty()
                        && binding!!.etAccountType.text.isNotEmpty()
                    ) {
                        validateButton(true)
                    }
                } else {
                    binding!!.llBankDetails.visibility = GONE
                    isIFSC_OK = false
                    validateButton(false)
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                isIFSC_OK = false
                binding!!.llBankDetails.visibility = GONE
                validateButton(false)
                // Handle the network error
            }
        })

    }

    fun returnOrder(o_id: Int, o_d_id: Int) {

        var jsonObject = JsonObject()
//        jsonObject.addProperty("product_detail_id", product_detail_id)
        if (o_d_id != 0) {
            jsonObject.addProperty("o_d_id", o_d_id)
        }
        jsonObject.addProperty("o_id", o_id)
        jsonObject.addProperty("account_holder_name", binding!!.etName.text.trim().toString())
        jsonObject.addProperty("account_number", binding!!.etAccountNumber.text.trim().toString())
        jsonObject.addProperty("account_type", binding!!.etAccountType.text.trim().toString())
        jsonObject.addProperty("acccount_ifsc_code", binding!!.etIFSCCode.text.trim().toString())

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, this@ReturnOrderActivity)
        var call: Call<CommonResponse> = apiInterface.returnOrder(jsonObject)!!

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                if (response.body()?.statuscode == 11) {
                    Toast.makeText(
                        this@ReturnOrderActivity,
                        "" + response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(
                        this@ReturnOrderActivity,
                        "" + response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    this@ReturnOrderActivity,
                    "" + t.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        })

    }


}