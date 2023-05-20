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
import com.matemart.activities.SearchActivity
import com.matemart.adapter.MainStoreAdapter
import com.matemart.databinding.FragmentHomeBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.*
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserProfile()
        getHomeData()

        binding?.header?.ivSearch?.setOnClickListener {
            startActivity(Intent(requireContext(),SearchActivity::class.java))
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
                call: Call<ResHomeList>,
                response: Response<ResHomeList>
            ) {
                if (response.body()?.statuscode == 11) {
                    response.body()?.data?.let {
                        val adapter = MainStoreAdapter(ArrayList(it), requireContext())
                        val layoutManager = LinearLayoutManager(requireContext())
                        binding?.rcMainStore?.setLayoutManager(layoutManager)
                        binding?.rcMainStore?.setAdapter(adapter)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResHomeList>, t: Throwable) {
                t.printStackTrace()
                Log.e("checkkFailed", "onFailure: " + t.message)
                Toast.makeText(
                    requireContext(),
                    "Something went wrong",
                    Toast.LENGTH_LONG
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
                call: Call<ResGetProfileDetails>,
                response: Response<ResGetProfileDetails>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {

                        Glide.with(requireContext()).load(it.profile_image)
                            .into(binding?.header?.ivProfile as ImageView)
                        binding?.header?.tvName?.text = it.uname
                    }


                } else {
                    Toast.makeText(
                        context,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetProfileDetails>, t: Throwable) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }

        })

    }


}