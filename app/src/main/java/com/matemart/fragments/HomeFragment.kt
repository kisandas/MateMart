package com.matemart.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.matemart.activities.LoginActivity
import com.matemart.activities.SearchActivity
import com.matemart.adapter.MainStoreAdapter
import com.matemart.databinding.FragmentHomeBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.*
import com.matemart.utils.MyApplication
import com.matemart.utils.Service
import com.matemart.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserProfile()


        binding?.header?.ivSearch?.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
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
                        val adapter = MainStoreAdapter(ArrayList(it), requireContext())
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

                            Glide.with(requireContext()).load(it.profile_image)
                                .into(binding?.header?.ivProfile as ImageView)
                            binding?.header?.tvName?.text = it.uname

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

                requireActivity().startActivity(
                    Intent(
                        requireContext(),
                        LoginActivity::class.java
                    )
                )
                requireActivity().finishAffinity()

                var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

                pref.write(SharedPrefHelper.ADDRESS_ID, 0)
                pref.write(SharedPrefHelper.EMAIL, "")
                pref.write(SharedPrefHelper.USER_ID, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_NUMBER, "")
                pref.write(SharedPrefHelper.KEY_LOGIN_TOKEN, "")
                pref.write(SharedPrefHelper.KEY_CCID, "")
                pref.write(SharedPrefHelper.KEY_LOGGED_IN, false)

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