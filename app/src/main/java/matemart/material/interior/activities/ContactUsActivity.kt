package matemart.material.interior.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import matemart.material.interior.databinding.ActivityContactUsBinding


class ContactUsActivity : AppCompatActivity() {

    private var binding: ActivityContactUsBinding? = null

    val Facebook_URL = "https://www.facebook.com/Matemartravi"
    val Instagram_URL = "https://www.instagram.com/thematemart?igsh=YW5hbHBkdTYzYnlo"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.llheaderlay.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding!!.llheaderlay.title.text = "Contact Us"

        binding!!.ivFacebook.setOnClickListener {
            startActivity(
                Intent(
                    this@ContactUsActivity,
                    WebViewActivity::class.java
                ).putExtra("url", Facebook_URL))

        }

        binding!!.ivInstagram.setOnClickListener {

            startActivity(
                Intent(
                    this@ContactUsActivity,
                    WebViewActivity::class.java
                ).putExtra("url", Instagram_URL))
        }


        binding!!.tvCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+91 76000 03515"))
            startActivity(intent)
        }

        binding!!.tvEmail.setOnClickListener {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "contact@matemart.in", null
                )
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body")
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }

    }
}