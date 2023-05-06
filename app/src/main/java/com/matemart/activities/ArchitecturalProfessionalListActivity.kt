package com.matemart.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.matemart.adapter.ArchitectureListAdapter
import com.matemart.databinding.ActivityArchitechturalProfessionalListBinding
import com.matemart.interfaces.ApiInterface
import com.matemart.model.Architect
import com.matemart.model.ResGetArchitectList
import com.matemart.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchitecturalProfessionalListActivity : AppCompatActivity() {
    var adapter: ArchitectureListAdapter? = null
    lateinit var binding: ActivityArchitechturalProfessionalListBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchitechturalProfessionalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.include5.ivBack.setOnClickListener() {
            finish()
        }

        binding.include5.title.text = "Architecture Professional"

        adapter = ArchitectureListAdapter(this, list, object : ArchitectureListAdapter.Item {
            override fun onClick(architect: Architect) {
                var intent: Intent = Intent(
                    this@ArchitecturalProfessionalListActivity,
                    ArchitecturalProfessionalDetailsActivity::class.java
                )
                intent.putExtra("data",  architect as java.io.Serializable)
                startActivity(intent)
            }
        })
        binding.list.adapter = adapter
        getArchitectList()
    }


    var list: MutableList<Architect> = arrayListOf()
    fun getArchitectList() {
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, this)
        var call: Call<ResGetArchitectList> = apiInterface.getArchitectList()!!

        call.enqueue(object : Callback<ResGetArchitectList> {
            override fun onResponse(
                call: Call<ResGetArchitectList>,
                response: Response<ResGetArchitectList>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { list.addAll(it) }
                    adapter!!.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@ArchitecturalProfessionalListActivity,
                        "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResGetArchitectList>, t: Throwable) {
                Toast.makeText(
                    this@ArchitecturalProfessionalListActivity,
                    "Something went wrong",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        })

    }
}