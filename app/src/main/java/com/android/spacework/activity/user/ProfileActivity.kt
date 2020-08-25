package com.android.spacework.activity.user

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPreferences = this.getSharedPreferences(
            Constants().USER_INFO_PREFERENCE,
            MODE_PRIVATE
        )

        setData()

        activity_profile_update.setOnClickListener {
            if(activity_profile_username.text.toString() == "" ||
                activity_profile_address.text.toString() == "") {
                Constants().generateErrorToast(
                    this,
                    this,
                    "Fill all the fields"
                )
                return@setOnClickListener
            }
            updateUser()
        }
    }

    private fun setData() {
        activity_profile_username.setText(sharedPreferences.getString(Constants().USER_NAME, ""))
        activity_profile_phoneNumber.setText(sharedPreferences.getString(Constants().USER_PHONENUMBER, ""))
        activity_profile_address.setText(sharedPreferences.getString(Constants().USER_ADDRESS, ""))
    }

    private fun updateUser() {
        activity_profile_update.startAnimation()
        val updateUserService = Constants().retrofit.create(API::class.java)
        updateUserService.updateUser(
            activity_profile_phoneNumber.text.toString(),
            activity_profile_address.text.toString(),
            activity_profile_username.text.toString()
        ).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    sharedPreferences.edit().putString(
                        Constants().USER_NAME,
                        activity_profile_username.text.toString()
                    ).apply()
                    sharedPreferences.edit().putString(
                        Constants().USER_ADDRESS,
                        activity_profile_address.text.toString()
                    ).apply()
                    setData()
                    Constants().generateSuccessToast(
                        this@ProfileActivity,
                        this@ProfileActivity,
                        response.body().toString()
                    )
                    activity_profile_update.revertAnimation()
                    activity_profile_update.setBackgroundResource(R.drawable.blue_fill__rounded_color)
                } else {
                    Constants().generateErrorToast(
                        this@ProfileActivity,
                        this@ProfileActivity,
                        response.errorBody().toString()
                    )
                    activity_profile_update.revertAnimation()
                    activity_profile_update.setBackgroundResource(R.drawable.blue_fill__rounded_color)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Constants().generateErrorToast(
                    this@ProfileActivity,
                    this@ProfileActivity,
                    t.localizedMessage
                )
                activity_profile_update.revertAnimation()
                activity_profile_update.setBackgroundResource(R.drawable.blue_fill__rounded_color)
            }

        })
    }
}