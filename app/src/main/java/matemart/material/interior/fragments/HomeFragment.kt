package matemart.material.interior.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import matemart.material.interior.R
import matemart.material.interior.activities.LocationActivity
import matemart.material.interior.activities.SearchActivity
import matemart.material.interior.databinding.FragmentHomeBinding
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.*
import matemart.material.interior.utils.MyApplication
import matemart.material.interior.utils.Service
import matemart.material.interior.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    var pref: SharedPrefHelper? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = SharedPrefHelper.getInstance(MyApplication.getInstance())
        getUserProfile()

        binding?.header?.ivLocation?.setOnClickListener {
            startActivity(Intent(requireContext(), LocationActivity::class.java))
        }

        binding?.header?.ivSearch?.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }

        if (pref?.read(SharedPrefHelper.IS_USER_GUEST, false) == true) {
            binding?.header?.ivLocation?.visibility = GONE
            binding?.header?.ivNotification?.visibility = GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    fun getHomeData() {
        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, requireContext())
        var call: Call<ResHomeList> = apiInterface.getHomeDataList()!!

        call.enqueue(object : Callback<ResHomeList> {
            override fun onResponse(
                call: Call<ResHomeList>, response: Response<ResHomeList>
            ) {

                Log.e("jjjjjjjjjjj", "onResponse: " + response.body()?.data.toString())
                Log.e("jjjjjjjjjjj", "onResponse: " + response.body()?.statuscode)
                if (response.body()?.statuscode == 11) {
                    response.body()?.data?.let {
                        val adapter = matemart.material.interior.adapter.MainStoreAdapter(
                            ArrayList(it),
                            requireContext()
                        )
                        val layoutManager = LinearLayoutManager(requireContext())
                        binding?.rcMainStore?.setLayoutManager(layoutManager)
                        binding?.rcMainStore?.setAdapter(adapter)
                    }
                } else if (response.body()?.statuscode == 44) {
                    logoutUser()

                } else {

                    Toast.makeText(
                        requireContext(), "Something went wrong", Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResHomeList>, t: Throwable) {
                Log.e("jjjjjjjjjjj", "onResponse: " + t.message)
                t.printStackTrace()
                Toast.makeText(
                    requireContext(), "Something went wrong", Toast.LENGTH_LONG
                ).show()
            }

        })

    }

    fun refreshToken(token: String) {

        var jsonObject = JsonObject()
        jsonObject.addProperty("device_key", token)

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, requireContext())
        var call: Call<CommonResponse> = apiInterface.refreshTokenForPush(jsonObject)!!

        Log.e("hhhhhhhhjjh", "refreshToken: " + jsonObject.toString())

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.e("hhhhhhhhjjh", "response: " + response.body().toString())

            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                Log.e("hhhhhhhhjjh", "errrrrrrr: " + t.message.toString())
            }

        })

    }


    private fun getUserProfile() {
        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, requireContext())
        var call: Call<ResGetProfileDetails> = apiInterface.getUserProfile()!!

        call.enqueue(object : Callback<ResGetProfileDetails> {
            override fun onResponse(
                call: Call<ResGetProfileDetails>, response: Response<ResGetProfileDetails>
            ) {
                Log.e("jjjjjjjjjjj", "onResponse: " + response.body()?.data.toString())
                Log.e("jjjjjjjjjjj", "onResponse: " + response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.statuscode == 11) {
                        response.body()?.data?.let {

                            Glide.with(requireContext()).load(it.profile_image).placeholder(
                                R.drawable.ic_user_place_holder
                            )
                                .into(binding?.header?.ivProfile as ImageView)
                            binding?.header?.tvName?.text = it.uname

                            refreshToken(
                                SharedPrefHelper.getInstance(MyApplication.getInstance())
                                    .read(SharedPrefHelper.FIREBASE_TOKEN)
                            )

                            SharedPrefHelper.getInstance(MyApplication.getInstance())
                                .write(SharedPrefHelper.USER_NAME, it.uname.toString()).toString()

                            SharedPrefHelper.getInstance(MyApplication.getInstance())
                                .write(SharedPrefHelper.EMAIL, it.email.toString()).toString()

                            it.a_id?.let { it1 ->
                                SharedPrefHelper.getInstance(MyApplication.getInstance())
                                    .write(SharedPrefHelper.ADDRESS_ID, it1)
                            }

                            getHomeData()
                        }
                    } else if (response.body()?.statuscode == 44) {
                        logoutUser()
                    }


                } else {
                    Toast.makeText(
                        context, "Something went wrong", Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetProfileDetails>, t: Throwable) {
                Log.e("jjjjjjjjjjj", "onFailure: " + t.message)
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun logoutUser() {

        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, requireContext())
        var call: Call<LogoutResponse> = apiInterface.logoutUser()!!

        call.enqueue(object : Callback<LogoutResponse> {
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>
            ) {

                Toast.makeText(
                    requireContext(),
                    response.body()?.message,
                    Toast.LENGTH_LONG
                ).show()


                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

                pref.logoutProfile(requireContext())
                requireActivity().finishAffinity()
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    t.toString(),
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }

}