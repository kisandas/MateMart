package matemart.material.interior.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE

import matemart.material.interior.model.AppDataResponse
import matemart.material.interior.utils.MyApplication
import matemart.material.interior.utils.SharedPrefHelper
import matemart.material.interior.databinding.ActivityAppUpdateBinding

class AppUpdateActivity : AppCompatActivity() {
    private var binding: ActivityAppUpdateBinding? = null
    var pref: SharedPrefHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppUpdateBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        pref = SharedPrefHelper.getInstance(MyApplication.getInstance())


        val updateData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("data", AppDataResponse::class.java)
        } else {
            intent.getSerializableExtra("data") as AppDataResponse
        }


        binding!!.tvVersion.text = "What's new in v" + updateData?.data?.appVersionData?.release

        if (updateData?.data?.appVersionData?.forceUpdate == true) {
            binding!!.tvNotNow.visibility = GONE
        } else {
            binding!!.tvNotNow.visibility = VISIBLE
        }

        binding!!.tvDescription.text = updateData?.data?.appVersionData?.description.toString()


        binding!!.tvNotNow.setOnClickListener {
            if (!pref?.read(SharedPrefHelper.KEY_ACCESS_TOKEN).isNullOrEmpty()) {
                startActivity(Intent(this@AppUpdateActivity, HomeActivity::class.java))
                finish()
            } else {
                startActivity(
                    Intent(
                        this@AppUpdateActivity,
                        LoginActivity::class.java
                    )
                )
                finish()
            }
        }

        binding!!.btnUpdate.setOnClickListener {
            var appURL = "https://play.google.com/store/apps/details?id=$packageName"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appURL))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}