package com.android.spacework.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_otp.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    private lateinit var userType: String
    private var phoneNumber : String = ""
    lateinit var verificationId : String
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        userType = intent.getStringExtra("userType")
        phoneNumber = intent.getStringExtra("phoneNumber")
        if(userType == "customer") {
            activity_otp_adminKeyLayout.visibility = View.GONE
        } else {
            activity_otp_adminKeyLayout.visibility = View.VISIBLE
        }

        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                activity_otp_otp.setText(p0!!.smsCode.toString())
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Constants().generateErrorToast(
                    this@OtpActivity,
                    this@OtpActivity,
                    p0!!.localizedMessage
                )
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                verificationId = p0!!
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            callback
        )

        activity_otp_continue.setOnClickListener {
            if(activity_otp_otp.text.toString() == "") {
                Toast.makeText(this, "Enter OTP first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(userType != "customer") {
                if(activity_otp_adminKey.text.toString() == "") {
                    Toast.makeText(this, "Enter admin key first", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if(activity_otp_adminKey.text.toString() != "iamadmin123") {
                    Toast.makeText(this, "Wrong admin passkey", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            activity_otp_continue.startAnimation()
            try {
                val credential = PhoneAuthProvider.getCredential(
                    verificationId,
                    activity_otp_otp.text.toString()
                )
                signInWithCredential(credential)
            } catch(e: Exception) {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential?) {
        auth.signInWithCredential(credential as AuthCredential)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val sharedPreferences = this.getSharedPreferences(
                        Constants().USER_INFO_PREFERENCE,
                        MODE_PRIVATE
                    )
                    sharedPreferences.edit().putString(
                        Constants().USER_PHONENUMBER,
                        phoneNumber
                    ).apply()
                    sharedPreferences.edit().putString(
                        Constants().USER_USERTYPE,
                        userType
                    ).apply()
                    val intent = Intent(this, UserDetailsActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Constants().generateErrorToast(
                        this,
                        this,
                        it!!.exception.toString()
                    )
                }
            }
    }
}