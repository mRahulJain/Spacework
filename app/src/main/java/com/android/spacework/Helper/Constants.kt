package com.android.spacework.Helper

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import kotlin.experimental.and

class Constants {

    //Create a app/res/xml/network_security_config.xml file
    //Add your IP_ADDRESS there
    //Add this information on your manifest file

    val IP_HOST = "http://192.168.29.193:3000"
    val retrofit = Retrofit.Builder()
        .baseUrl(IP_HOST)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun generateProductId(productName: String) : String {
        return EncryptHelper().hash(productName)
    }
}