package matemart.material.Interior.model

import com.google.gson.annotations.SerializedName


data class AddressListResponse(

    @SerializedName("statuscode") var statuscode: Int? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<Address> = arrayListOf()
)


data class Address(

    @SerializedName("a_id") var aId: Int? = null,
    @SerializedName("u_id") var uId: Int? = null,
    @SerializedName("flat_no") var flatNo: String? = null,
    @SerializedName("fullname") var fullname: String? = null,
    @SerializedName("a_email") var aEmail: String? = null,
    @SerializedName("a_mobile") var aMobile: String? = null,
    @SerializedName("state") var state: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("pincode") var pincode: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("default") var default: Int? = null,
    @SerializedName("type_of_address") var typeOfAddress: Int? = null,
    @SerializedName("lat") var lat: Double? = null,
    @SerializedName("long") var long: Double? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null

):java.io.Serializable