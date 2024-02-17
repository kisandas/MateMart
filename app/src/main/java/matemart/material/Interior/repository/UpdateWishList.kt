package matemart.material.Interior.repository

import android.content.Context
import com.google.gson.JsonObject
import matemart.material.Interior.interfaces.ApiInterface
import matemart.material.Interior.utils.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateWishList {

    lateinit var context: Context
    lateinit var event: Event

    public constructor(context: Context, event: Event) {
        this.context = context
        this.event = event
    }


    public fun updateWishList(productId: Int, is_wishlist: Int) {
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("p_id", productId)
        jsonObject.addProperty("is_wishlist", is_wishlist)
        var apiInterface: ApiInterface = Service.createService(ApiInterface::class.java, context)

        var call: Call<JsonObject> = apiInterface.updateWishList(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    event.OnUpdate()
                } else {
                    event.OnFail()
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                event.OnFail()
            }
        })


    }


    public interface Event {
        public fun OnUpdate()
        public fun OnFail()
    }

}