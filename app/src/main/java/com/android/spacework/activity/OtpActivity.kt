package com.android.spacework.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.android.spacework.R
import kotlinx.android.synthetic.main.activity_otp.*
import www.sanju.motiontoast.MotionToast

class OtpActivity : AppCompatActivity() {

    private lateinit var userType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        userType = intent.getStringExtra("userType")
        if(userType == "customer") {
            activity_otp_adminKeyLayout.visibility = View.GONE
        } else {
            activity_otp_adminKeyLayout.visibility = View.VISIBLE
        }

        activity_otp_continue.setOnClickListener {
//            activity_otp_continue.startAnimation()
//            MotionToast.createToast(this,"Upload Completed!",
//                MotionToast.TOAST_SUCCESS,
//                MotionToast.GRAVITY_BOTTOM,
//                MotionToast.LONG_DURATION,
//                ResourcesCompat.getFont(this,R.font.helvetica_regular))

            val intent = Intent(this, AppActivity::class.java)
            startActivity(intent)
        }
    }
}