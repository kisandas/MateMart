package com.matemart.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


//This is application class used to create some instances used in whole application
@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        private lateinit var currentApplication: MyApplication


        fun getInstance(): MyApplication {
            return currentApplication
        }
    }


    override fun onCreate() {
        super.onCreate()
        currentApplication = this


    }

    // get instance of preferences
    fun getSharedPrefInstance(): SharedPrefHelper {
        return SharedPrefHelper.getInstance(this)
    }






}

