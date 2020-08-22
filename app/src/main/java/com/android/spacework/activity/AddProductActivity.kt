package com.android.spacework.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.model.Product
import kotlinx.android.synthetic.main.activity_add_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        activity_add_product_add.setOnClickListener {
            if(activity_add_product_name.text.toString() == "" ||
                activity_add_product_description.text.toString() == "" ||
                activity_add_product_price.text.toString() == "" ||
                activity_add_product_imageLink.text.toString() == "") {
                MotionToast.createToast(this,"Fill all the fields",
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.helvetica_regular))
                return@setOnClickListener
            }

            val addProductService = Constants().retrofit.create(API::class.java)
            addProductService.addProduct(
                Constants().generateProductId(activity_add_product_name.text.toString()),
                activity_add_product_name.text.toString(),
                activity_add_product_description.text.toString(),
                activity_add_product_price.text.toString().toDouble(),
                activity_add_product_isAvailable.isChecked,
                activity_add_product_imageLink.text.toString()
            ).enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("myError", t.localizedMessage)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response!!.isSuccessful) {
                        if(response.body().toString() == "Product added") {
                            MotionToast.createToast(this@AddProductActivity,response.body().toString(),
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@AddProductActivity,R.font.helvetica_regular))
                        } else {
                            MotionToast.createToast(this@AddProductActivity,response.body().toString(),
                                MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@AddProductActivity,R.font.helvetica_regular))
                        }
                    } else {
                        val error = response!!.errorBody()
                        MotionToast.createToast(this@AddProductActivity,error.toString(),
                            MotionToast.TOAST_ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@AddProductActivity,R.font.helvetica_regular))
                    }
                }
            })
        }
    }
}