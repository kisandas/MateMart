package com.matemart.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.matemart.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private var binding: ActivityWebViewBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        var url = intent?.getStringExtra("url")
        binding?.webView?.settings?.javaScriptEnabled = true // enable javascript


        binding?.webView?.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                Toast.makeText(this@WebViewActivity, description, Toast.LENGTH_SHORT).show()
            }


            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (request?.url.toString().startsWith("matemart://razorpay/success")) {

                    val uri: Uri = Uri.parse(request?.url.toString())

                    // Extract query parameters

                    // Extract query parameters
                    val order_number: String? = uri.getQueryParameter("order_number")
                    val amount: String? = uri.getQueryParameter("amount")
                    val date: String? = uri.getQueryParameter("date")

                    Log.e("checkURLLLLL", "shouldOverrideUrlLoading Successss: order_number="+order_number+"     amount="+amount+"      date="+date )

                    return true
                } else if (request?.url.toString().startsWith("matemart://razorpay/failed")) {

                    val uri: Uri = Uri.parse(request?.url.toString())

                    val order_number: String? = uri.getQueryParameter("order_number")
                    val amount: String? = uri.getQueryParameter("amount")
                    val date: String? = uri.getQueryParameter("date")
                    Log.e("checkURLLLLL", "shouldOverrideUrlLoading  Failed  : order_number="+order_number+"     amount="+amount+"      date="+date )

                    return true
                } else {
                    return super.shouldOverrideUrlLoading(view, request)
                }


            }

            override fun onReceivedError(
                view: WebView?,
                req: WebResourceRequest,
                rerr: WebResourceError
            ) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(
                    view,
                    rerr.errorCode,
                    rerr.description.toString(),
                    req.url.toString()
                )
            }
        }

        url?.let { binding?.webView?.loadUrl(it) }

    }
}