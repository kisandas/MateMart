package matemart.material.Interior.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import matemart.material.Interior.databinding.ActivityPaymentFailedBinding

class PaymentFailedActivity : AppCompatActivity() {

    private var binding: ActivityPaymentFailedBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentFailedBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.llheaderlay.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding!!.llheaderlay.title.text = "Failed"
    }
}