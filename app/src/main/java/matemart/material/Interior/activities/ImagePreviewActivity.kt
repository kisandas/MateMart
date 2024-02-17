package matemart.material.Interior.activities

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import matemart.material.Interior.R
import matemart.material.Interior.adapter.ImagePreviewSliderAdapter

class ImagePreviewActivity : AppCompatActivity() , ImagePreviewSliderAdapter.OnItemClickListener {
    lateinit var  imageView :ImageView
    lateinit var   rcImageList :RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the activity full-screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_image_preview)

        imageView = findViewById<ImageView>(R.id.imageView)
        rcImageList = findViewById<RecyclerView>(R.id.rcImageList)

        // Get the image URL from the intent
        val itemList = intent.getStringArrayListExtra("imageUrl")
        val position = intent.getIntExtra("position",0)


        rcImageList.apply {
            val adapter = itemList?.let {
                ImagePreviewSliderAdapter(
                    this@ImagePreviewActivity,
                    it,
                    position,
                    this@ImagePreviewActivity
                )
            }
            layoutManager =
                LinearLayoutManager(this@ImagePreviewActivity, RecyclerView.HORIZONTAL, false)
            rcImageList.adapter = adapter

        }

        if(!itemList.isNullOrEmpty()){
            Glide.with(this)
                .load(itemList[position])
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }




        // Load and display the image using Glide or your preferred image loading library


        // Set a click listener to close the activity when the image is clicked

    }

    override fun onItemClick(position: Int, itemList: List<String>) {
        Glide.with(this)
            .load(itemList[position])
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}