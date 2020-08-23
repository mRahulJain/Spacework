package com.android.spacework.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.spacework.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        activity_login_getOtp.setOnClickListener {
            val intent = Intent(this, OtpActivity::class.java)
            if(activity_login_adminCheck.isChecked) {
                intent.putExtra("userType", "admin")
            } else {
                intent.putExtra("userType", "customer")
            }
            startActivity(intent)
        }
    }
}