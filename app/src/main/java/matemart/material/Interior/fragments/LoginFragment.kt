package matemart.material.Interior.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import matemart.material.Interior.activities.OTPActivity
import androidx.fragment.app.activityViewModels
import matemart.material.Interior.activities.HomeActivity
import matemart.material.Interior.databinding.FragmentLoginBinding
import matemart.material.Interior.interfaces.ApiInterface
import matemart.material.Interior.model.GuestUserModel
import matemart.material.Interior.model.login.LoginResponse
import matemart.material.Interior.utils.BaseFragment
import matemart.material.Interior.utils.MyApplication
import matemart.material.Interior.utils.Service
import matemart.material.Interior.utils.SharedPrefHelper
import matemart.material.Interior.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class LoginFragment : BaseFragment() {


    private val viewModel: AuthViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding
    override fun observeViewModel() {
        TODO("Not yet implemented")
    }

    override fun initViewBinding(): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getRootView(): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return initViewBinding()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener(View.OnClickListener {
            checkValidation()
            //                startActivity(new Intent(getContext(), OTPActivity.class));
        })


        binding.tvGuestUser.setOnClickListener {
            getGuestUserLogin()
        }
    }

    fun getGuestUserLogin() {
        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, requireContext())
        var call: Call<GuestUserModel> = apiInterface.getGuestUser()!!

        call.enqueue(object : Callback<GuestUserModel> {
            override fun onResponse(
                call: Call<GuestUserModel>,
                response: Response<GuestUserModel>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.data?.apiToken

                    token?.let {
                        SharedPrefHelper.getInstance(MyApplication.getInstance())
                            .write(SharedPrefHelper.KEY_ACCESS_TOKEN, it)
                    }

                    SharedPrefHelper.getInstance(MyApplication.getInstance())
                        .write(SharedPrefHelper.IS_USER_GUEST, true)

                    startActivity(Intent(requireContext(), HomeActivity::class.java))
                    requireActivity().finish()
                } else {

                }
            }

            override fun onFailure(call: Call<GuestUserModel>, t: Throwable) {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }

        })

    }

    var phoneNo = ""
    private fun checkValidation() {
        if (binding.etNumber.text.toString()
                .isEmpty() || binding.etNumber.text.toString().length != 10
        ) {
            matemart.material.Interior.utils.Toast.Toaster.Builder(requireActivity())
                .setTitle("ERROR")
                .setDescription("Please Enter Valid Number")
                .setDuration(5000)
                .setStatus(matemart.material.Interior.utils.Toast.Toaster.Status.ERROR)
                .show()
            return
        }

        viewModel.login(binding.tvCountryCode.text.toString() + binding.etNumber.text.toString())
        phoneNo = binding.tvCountryCode.text.toString() + binding.etNumber.text.toString()
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            if (it?.data?.statuscode == 11) {
                successResponse(it.data as LoginResponse)
            } else {
                matemart.material.Interior.utils.Toast.Toaster.Builder(requireActivity())
                    .setTitle("ERROR")
                    .setDescription(it?.data?.message)
                    .setDuration(5000)
                    .setStatus(matemart.material.Interior.utils.Toast.Toaster.Status.ERROR)
                    .show()
            }
        }

    }

    private fun successResponse(response: LoginResponse) {

        val token = response.data?.token?.toString()

        token?.let {
            SharedPrefHelper.getInstance(MyApplication.getInstance())
                .write(SharedPrefHelper.KEY_LOGIN_TOKEN, it)
        }

        Log.e("checkToken", "onResponse: ${response.toString()}")

        response.data?.uId?.let {
            SharedPrefHelper.getInstance(MyApplication.getInstance())
                .write(SharedPrefHelper.USER_ID, it)
        }

        SharedPrefHelper.getInstance(MyApplication.getInstance())
            .write(SharedPrefHelper.IS_USER_GUEST, false)

        SharedPrefHelper.getInstance(MyApplication.getInstance())
            .write(
                SharedPrefHelper.KEY_LOGIN_NUMBER,
                binding.tvCountryCode.text.toString() + binding.etNumber.text.toString()
            )

        Log.e("checkToken", "onResponse: $token")
        matemart.material.Interior.utils.Toast.Toaster.Builder(requireActivity())
            .setTitle("Success")
            .setDescription("message")
            .setDuration(5000)
            .setStatus(matemart.material.Interior.utils.Toast.Toaster.Status.SUCCESS)
            .show()
        startActivity(
            Intent(requireActivity(), OTPActivity::class.java).putExtra(
                "phoneNo",
                phoneNo
            )
        )
    }
}