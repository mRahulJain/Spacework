package com.android.spacework.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.activity.user.AppActivity
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_user_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailsActivity : AppCompatActivity() {

    private var messageToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val sharedPreferences = this.getSharedPreferences(
            Constants().USER_INFO_PREFERENCE,
            MODE_PRIVATE
        )

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                Log.d("myCHECK", "INSIDE ON COMPLETE")
                if(!it.isSuccessful) {
                    Log.e("myError", "There occurred some problem")
                    return@addOnCompleteListener
                }
                messageToken = it.result!!.token
            }

        activity_user_details_proceed.setOnClickListener {
            if(activity_user_details_name.text.toString() == "" ||
                activity_user_details_address.text.toString() == "") {
                Constants().generateErrorToast(
                    this,
                    this,
                    "Please fill all the fields"
                )
            }
            activity_user_details_proceed.startAnimation()
            sharedPreferences.edit().putString(
                Constants().USER_NAME,
                activity_user_details_name.text.toString()
            ).apply()
            sharedPreferences.edit().putString(
                Constants().USER_ADDRESS,
                activity_user_details_address.text.toString()
            ).apply()
            val phoneNumber = sharedPreferences.getString(Constants().USER_PHONENUMBER, "")

            val addUserService = Constants().retrofit.create(API::class.java)
            addUserService.addUser(
                phoneNumber!!,
                activity_user_details_name.text.toString(),
                activity_user_details_address.text.toString(),
                messageToken
            ).enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.isSuccessful) {
                        activity_user_details_proceed.revertAnimation()
                        activity_user_details_proceed.setBackgroundResource(R.drawable.blue_fill__rounded_color)
                        val intent = Intent(this@UserDetailsActivity, AppActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Constants().generateErrorToast(
                            this@UserDetailsActivity,
                            this@UserDetailsActivity,
                            response!!.errorBody().toString()
                        )
                        activity_user_details_proceed.revertAnimation()
                        activity_user_details_proceed.setBackgroundResource(R.drawable.blue_fill__rounded_color)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Constants().generateErrorToast(
                        this@UserDetailsActivity,
                        this@UserDetailsActivity,
                        t.localizedMessage
                    )
                    activity_user_details_proceed.revertAnimation()
                    activity_user_details_proceed.setBackgroundResource(R.drawable.blue_fill__rounded_color)
                }

            })
        }
    }
}