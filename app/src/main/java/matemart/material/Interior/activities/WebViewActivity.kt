package matemart.material.Interior.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import matemart.material.Interior.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private var binding: ActivityWebViewBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var url = intent?.getStringExtra("url")
        binding?.webView?.settings?.javaScriptEnabled = true // enable javascript


        binding?.llheaderlay?.ivBack?.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding?.webView?.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                Log.e("webURLLLLL", "shouldOverrideUrlLoading: errorCode:    " + errorCode)
                Toast.makeText(this@WebViewActivity, description, Toast.LENGTH_SHORT).show()
            }


            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {

                if (request?.url.toString().startsWith("matemart://ccavenue/success")) {

                    val uri: Uri = Uri.parse(request?.url.toString())

                    binding?.llheaderlay?.title?.text = view?.title

                    // Extract query parameters

                    // Extract query parameters
                    val order_number: String? = uri.getQueryParameter("order_number")
                    val amount: String? = uri.getQueryParameter("amount")
                    val date: String? = uri.getQueryParameter("date")

                    startActivity(
                        Intent(this@WebViewActivity, PaymentSuccessActivity::class.java)
                            .putExtra("order_number", order_number)
                            .putExtra("amount", amount)
                            .putExtra("date", date)
                    )
                    finish()

                    Log.e(
                        "checkURLLLLL",
                        "shouldOverrideUrlLoading Successss: order_number=" + order_number + "     amount=" + amount + "      date=" + date
                    )

                    return true
                } else if (request?.url.toString().startsWith("matemart://ccavenue/failed")) {

                    val uri: Uri = Uri.parse(request?.url.toString())

                    val order_number: String? = uri.getQueryParameter("order_number")
                    val amount: String? = uri.getQueryParameter("amount")
                    val date: String? = uri.getQueryParameter("date")

                    startActivity(Intent(this@WebViewActivity, PaymentFailedActivity::class.java))
                    finish()
                    Log.e(
                        "checkURLLLLL",
                        "shouldOverrideUrlLoading  Failed  : order_number=" + order_number + "     amount=" + amount + "      date=" + date
                    )

                    return true
                } else if (request?.url.toString().startsWith("https://www.facebook.com")) {
                    binding?.llheaderlay?.title?.text = "Facebook"
                    return super.shouldOverrideUrlLoading(view, request)
                } else if (request?.url.toString().startsWith("https://www.instagram.com")) {
                    binding?.llheaderlay?.title?.text = "Instagram"
                    return super.shouldOverrideUrlLoading(view, request)
                } else if (!request?.url.toString().startsWith("http")) {
                    if(request?.url.toString().contains("fb",true) || request?.url.toString().contains("facebook",true)){
                        binding?.llheaderlay?.title?.text = "Facebook"

                    }else if(request?.url.toString().contains("instagram",true)){
                        binding?.llheaderlay?.title?.text = "Facebook"
                    }
                    return true
                } else{
                    binding?.llheaderlay?.title?.text = view?.title
                    return super.shouldOverrideUrlLoading(view, request)
                }


            }

            override fun onReceivedError(
                view: WebView?,
                req: WebResourceRequest,
                rerr: WebResourceError
            ) {
                // Redirect to deprecated method, so you can use it in all SDK versions
//                onReceivedError(
//                    view,
//                    rerr.errorCode,
//                    rerr.description.toString(),
//                    req.url.toString()
//                )
            }
        }


        binding?.llheaderlay?.ivBack?.setOnClickListener {
            if (binding?.webView?.canGoBack() == true) {
                binding?.webView?.goBack()
            }else{
                onBackPressedDispatcher.onBackPressed()
            }
        }
        url?.let { binding?.webView?.loadUrl(it) }

    }
}