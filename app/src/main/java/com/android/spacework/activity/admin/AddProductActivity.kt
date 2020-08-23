package com.android.spacework.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import kotlinx.android.synthetic.main.activity_add_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast

class AddProductActivity : AppCompatActivity() {

    private val categoryList = arrayListOf(
        "SELECT PRODUCT CATEGORY", "Water", "Cold Drink"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setSpinnerView()

        activity_add_product_add.setOnClickListener {
            if(activity_add_product_name.text.toString() == "" ||
                activity_add_product_description.text.toString() == "" ||
                activity_add_product_price.text.toString() == "" ||
                activity_add_product_imageLink.text.toString() == "" ||
                activity_add_product_productCategory.selectedItem.toString() == "SELECT PRODUCT CATEGORY") {
                MotionToast.createToast(this,"Fill all the fields",
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.helvetica_regular))
                return@setOnClickListener
            }
            addProduct()
        }
    }

    private fun clearEverything() {
        activity_add_product_name.setText("")
        activity_add_product_description.setText("")
        activity_add_product_price.setText("")
        activity_add_product_imageLink.setText("")
        setSpinnerView()
        activity_add_product_isAvailable.isChecked = false
    }

    private fun addProduct() {
        activity_add_product_add.startAnimation()
        val addProductService = Constants().retrofit.create(API::class.java)
        addProductService.addProduct(
            Constants().generateProductId(activity_add_product_name.text.toString()),
            activity_add_product_name.text.toString(),
            activity_add_product_description.text.toString(),
            activity_add_product_price.text.toString().toDouble(),
            activity_add_product_isAvailable.isChecked,
            activity_add_product_imageLink.text.toString(),
            activity_add_product_productCategory.selectedItem.toString()
        ).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("myError", t.localizedMessage)
                activity_add_product_add.revertAnimation()
                activity_add_product_add.setBackgroundResource(R.drawable.blue_fill__rounded_color)
            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response!!.isSuccessful) {
                    if(response.body().toString() == "Product added") {
                        MotionToast.createToast(this@AddProductActivity,response.body().toString(),
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@AddProductActivity,R.font.helvetica_regular))
                        clearEverything()
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
                activity_add_product_add.revertAnimation()
                activity_add_product_add.setBackgroundResource(R.drawable.blue_fill__rounded_color)
            }
        })
    }

    private fun setSpinnerView() {
        activity_add_product_productCategory.adapter = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            categoryList
        )
    }
}