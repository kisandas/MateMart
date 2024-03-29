package matemart.material.interior.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import matemart.material.interior.activities.*
import matemart.material.interior.databinding.FragmentMyProfileBinding
import matemart.material.interior.interfaces.ApiInterface
import matemart.material.interior.model.LogoutResponse
import matemart.material.interior.model.ResGetProfileDetails
import matemart.material.interior.model.UserProfile
import matemart.material.interior.utils.MyApplication
import matemart.material.interior.utils.Service
import matemart.material.interior.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileFragment : Fragment() {
    private var binding: FragmentMyProfileBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getUserProfile()
    }


    override fun getUserVisibleHint(): Boolean {
        return super.getUserVisibleHint()

    }

    fun getUserProfile() {
        var apiInterface: ApiInterface =
            Service.createService(ApiInterface::class.java, requireContext())
        var call: Call<ResGetProfileDetails> = apiInterface.getUserProfile()!!

        call.enqueue(object : Callback<ResGetProfileDetails> {
            override fun onResponse(
                call: Call<ResGetProfileDetails>,
                response: Response<ResGetProfileDetails>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { setDefaultData(it) }


                } else {
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetProfileDetails>, t: Throwable) {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                    .show()
            }

        })

    }


    lateinit var userProfile: UserProfile
    var imageUri: Uri? = null
    fun setDefaultData(userProfile: UserProfile) {
        this.userProfile = userProfile

        imageUri = null
        var split: List<String> = userProfile.uname?.split(" ")!!
        if (split.isNotEmpty()) {
            binding!!.tvName.setText(userProfile.uname)



            binding!!.tvGuestEmail.setText(userProfile.email)
            Glide.with(requireContext())
                .load(userProfile.profile_image).into(binding!!.circleImageView)
            binding!!.tvName.text = userProfile.uname
        }
    }

    fun initView() {

        if (SharedPrefHelper.getInstance(MyApplication.getInstance())
                .read(SharedPrefHelper.IS_USER_GUEST, false)
        ) {
            binding!!.mainLayoutOptions.visibility = GONE
            binding!!.btnLogin.visibility = VISIBLE
            binding!!.tvEdit.visibility = GONE
            binding!!.tvGuestEmail.visibility = GONE

        } else {
            binding!!.btnLogin.visibility = GONE
            binding!!.tvEdit.visibility = VISIBLE
            binding!!.tvGuestEmail.visibility = VISIBLE
            binding!!.mainLayoutOptions.visibility = VISIBLE
        }

        binding!!.include2.title.text = "My Profile"
        binding!!.include2.ivBack.visibility = INVISIBLE

        binding!!.btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        SharedPrefHelper.getInstance(MyApplication.getInstance())
            .read(SharedPrefHelper.KEY_ACCESS_TOKEN)
        binding!!.tvWishList.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    WishListActivity::class.java
                )
            )
        }
        binding!!.tvAddress.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    AddressListActivity::class.java
                )
            )
        }
        binding!!.tvEdit.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    ProfileActivity::class.java
                )
            )
        }
        binding!!.tvPostRequirements.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    PostYourRequirements::class.java
                )
            )
        }
        binding!!.tvArch.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    ArchitecturalProfessionalListActivity::class.java
                )
            )
        }
        binding!!.tvLabour.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    LabouresListActivity::class.java
                )
            )
        }
        binding!!.tvPolicy.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    PolicyDetailsActivity::class.java
                )
            )
        }
        binding!!.tvContactUs.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    ContactUsActivity::class.java
                )
            )
        }

        binding!!.btnLogout.setOnClickListener {
            logoutUser()
        }

        binding!!.tvDeleteAccount.setOnClickListener {
            deleteAccount()
        }
    }

    private fun deleteAccount() {
        startActivity(Intent(requireContext(), matemart.material.interior.activities.AccountDeleteActivity::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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
                if (response.body()?.statuscode == 11) {

                    Toast.makeText(
                        requireContext(),
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()



                    var pref = SharedPrefHelper.getInstance(MyApplication.getInstance())

                    pref.logoutProfile(requireContext())
                    requireActivity().finishAffinity()


                } else {
                    Toast.makeText(
                        requireContext(),
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
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