package com.matemart.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.matemart.activities.SplashActivity


/**
 * This is shared prefences class used to store some values in local storage like access token and used detail
 * it have read and write methods foe int, string and boolean values. You can also create methods to store other types of data.
 */

class SharedPrefHelper private constructor(application: MyApplication) {


    private var sharedPref: SharedPreferences =
        application.getSharedPreferences(application.packageName, Activity.MODE_PRIVATE)


    companion object :
        SingletonHolder<SharedPrefHelper, MyApplication>(::SharedPrefHelper) {

        const val KEY_LOGGED_IN = "key_logged_in"
        const val KEY_ACCESS_TOKEN = "key_access_token"
        const val USER_NAME = "u_name"
        const val KEY_MEMBER = "KEY_MEMBER"
        const val KEY_FCM_TOKEN = "KEY_FCM_TOKEN"
        const val IS_GUEST = "IS_GUEST"
        const val LAT = "LAT"
        const val LONG = "LONG"
        const val ADDRESS_CURRENT = "ADDRESS_CURRENT"
        const val LANGUAGE = "language"
        const val SUB_TOKEN = "SUB_TOKEN"
        const val EMAIL = "email"
        const val MINISERIAL = "miniSerial"
        const val MINI_BATTERY = "miniBattery"
        const val IS_DEVICE_REGISTERED = "isDeviceRegistered"
        const val USER_ID = "u_id"
        const val ADDRESS_ID = "a_id"
        const val FIREBASE_TOKEN = "firebaseToken"
        var KEY_LOGIN_TOKEN = "key_otp_token"
        var IS_USER_GUEST = "is_user_guest"
        var KEY_LOGIN_NUMBER = "key_number"
        var KEY_STATE = "key_state"
        var KEY_CITY = "key_city"
        var KEY_PINCODE = "key_pincode"
        var KEY_CCID = "key_ccid"

    }

    fun read(key: String?, defValue: String?): String? {
        return sharedPref.getString(key, defValue)
    }

    fun write(key: String, value: String) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun read(key: String, defValue: Boolean): Boolean {
        return sharedPref.getBoolean(key, defValue)
    }

    fun write(key: String, value: Boolean) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }

    //


    fun read(key: String?): String {

        return sharedPref.getString(key, "")!!
    }


    fun read(key: String?, defValue: Long): Long? {
        return sharedPref.getLong(key, defValue)
    }


    fun read(key: String?, defValue: Int): Int? {
        return sharedPref.getInt(key, defValue)
    }

    fun write(key: String?, value: Long) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putLong(key, value)
        prefsEditor.apply()
    }

    fun write(key: String?, value: Int) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putInt(key, value)
        prefsEditor.apply()
    }

    fun clear() {
        val lat = SharedPrefHelper.getInstance(MyApplication.getInstance())
            .read(LAT, "").toString()

        val lang = SharedPrefHelper.getInstance(MyApplication.getInstance())
            .read(LANGUAGE, "en").toString()

        val longi = SharedPrefHelper.getInstance(MyApplication.getInstance())
            .read(LONG, "")
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.clear()
        prefsEditor.putString(LAT, lat)
        prefsEditor.putString(LONG, longi)
        prefsEditor.putString(LANGUAGE, lang)

        prefsEditor.apply()
    }

    fun write(s: String, maxDepthFeet: Float) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putFloat(s, maxDepthFeet)
        prefsEditor.apply()
    }

    fun read(key: String, defValue: Float): Float {
        return sharedPref.getFloat(key, defValue)
    }

    fun logoutProfile(context: Context) {
        sharedPref.all.clear()
        val intent = Intent(context, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

}