package com.android.spacework.Helper

import android.app.Activity
import android.content.Context
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import com.android.spacework.R
import kotlinx.android.synthetic.main.fragment_cart.view.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import www.sanju.motiontoast.MotionToast
import java.security.MessageDigest
import kotlin.experimental.and

class Constants {

    //Create a app/res/xml/network_security_config.xml file
    //Add your IP_ADDRESS there
    //Add this information on your manifest file

    //BASE URL
    //val IP_HOST = "http://192.168.29.193:3000"
    val IP_HOST = "https://app-spacework.herokuapp.com"

    val retrofit = Retrofit.Builder()
        .baseUrl(IP_HOST)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val USER_INFO_PREFERENCE: String = "USER_INFO_PREFERENCE"
    val USER_PHONENUMBER : String = "USER_EMAIL"
    val USER_NAME : String = "USER_NAME"
    val USER_ADDRESS: String = "USER_ADDRESS"
    val USER_USERTYPE: String = "USER_USERTYPE"
    val USER_USERCART: String = "USER_USERCART"
    val delay: Long = 2000


    fun generateProductId(productName: String) : String {
        return EncryptHelper().hash(productName)
    }

    fun generateErrorToast(activity: Activity, context: Context, text: String) {
        MotionToast.createColorToast(
            activity,
            text,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(context, R.font.helvetica_regular)
        )
    }

    fun generateSuccessToast(activity: Activity, context: Context, text: String) {
        MotionToast.createColorToast(
            activity,
            text,
            MotionToast.TOAST_SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(context, R.font.helvetica_regular)
        )
    }

    fun progressAnimation(activity: Activity, progressBar: ProgressBar) {
        val anim = ProgressBarAnim(activity!!, progressBar, 0f, 100f)
        anim.duration = delay
        progressBar.animation = anim
    }
}