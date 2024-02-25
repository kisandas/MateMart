package matemart.material.interior.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import matemart.material.interior.databinding.FragmentMyOrderBinding
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.AllOrderResponseResponse
import matemart.material.interior.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrderFragment : Fragment() {
    private var binding: FragmentMyOrderBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentMyOrderBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllOrderData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


    fun getAllOrderData(){

            var apiInterface: ApiInterface =
                Service.createService(ApiInterface::class.java, requireContext())
            var call: Call<AllOrderResponseResponse> = apiInterface.getAllOrderData()!!

            call.enqueue(object : Callback<AllOrderResponseResponse> {
                override fun onResponse(
                    call: Call<AllOrderResponseResponse>,
                    response: Response<AllOrderResponseResponse>
                ) {
                    if (response.body()?.statuscode == 11) {
                        response.body()?.data?.let {

                            if (it.isNotEmpty()) {
                                binding!!.rvOrderData.visibility = VISIBLE
                                binding!!.llEmptView.visibility = GONE

                                var orderDataList = ArrayList(it)

                                Log.e("hhhhhh", "onResponse: "+orderDataList.size )
                                Log.e("hhhhhh", "onResponse: "+orderDataList.toString() )
                                binding!!.rvOrderData.apply {
                                    layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                                    adapter =
                                        matemart.material.interior.adapter.OrderDataListAdapter(
                                            orderDataList,
                                            requireContext()
                                        )
                                }


                            }

                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Something went wrong",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<AllOrderResponseResponse>, t: Throwable) {
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
}