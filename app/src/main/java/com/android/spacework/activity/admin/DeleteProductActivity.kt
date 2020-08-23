package com.android.spacework.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.model.Product
import kotlinx.android.synthetic.main.activity_delete_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteProductActivity : AppCompatActivity() {

    private val categoryList = arrayListOf(
        "SELECT PRODUCT CATEGORY", "Water", "Cold Drink"
    )

    private var productNameList = arrayListOf(
        "SELECT PRODUCT NAME"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_product)
        setProductCategorySpinnerView()
        setProductNameSpinnerView()

        activity_delete_product_productCategory.onItemSelectedListener = spinnerProductCategoryListener()

        activity_delete_product_delete.setOnClickListener {
            if(activity_delete_product_productCategory.selectedItem.toString() == "SELECT PRODUCT CATEGORY" ||
                activity_delete_product_productName.selectedItem.toString() == "SELECT PRODUCT NAME") {
                Constants().generateErrorToast(this, this, "No item selected")
                return@setOnClickListener
            }
            val productId = Constants().generateProductId(activity_delete_product_productName.selectedItem.toString())
            deleteProduct(productId)
        }
    }

    private fun deleteProduct(productId: String) {
        activity_delete_product_delete.startAnimation()
        val deleteProductService = Constants().retrofit.create(API::class.java)
        deleteProductService.deleteProduct(productId).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    Constants().generateSuccessToast(
                        this@DeleteProductActivity,
                        this@DeleteProductActivity,
                        response.body().toString()
                    )
                    productNameList.clear()
                    productNameList.add("SELECT PRODUCT NAME")
                    setProductNameSpinnerView()
                    setProductCategorySpinnerView()
                    activity_delete_product_delete.revertAnimation()
                    activity_delete_product_delete.setBackgroundResource(R.drawable.blue_fill__rounded_color)
                } else {
                    Log.e("myERROR", response.errorBody().toString())
                    activity_delete_product_delete.revertAnimation()
                    activity_delete_product_delete.setBackgroundResource(R.drawable.blue_fill__rounded_color)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("myERROR", t.localizedMessage)
                activity_delete_product_delete.stopAnimation()
                activity_delete_product_delete.setBackgroundResource(R.drawable.blue_fill__rounded_color)
            }

        })
    }

    private fun spinnerProductCategoryListener() : AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p0!!.selectedItem.toString() != "SELECT PRODUCT CATEGORY") {
                    val getProductByCategoryService = Constants().retrofit.create(API::class.java)
                    getProductByCategoryService.getProductByCategory(p0!!.selectedItem.toString())
                        .enqueue(object : Callback<Array<Product>> {
                            override fun onResponse(
                                call: Call<Array<Product>>,
                                response: Response<Array<Product>>
                            ) {
                                if(response!!.isSuccessful) {
                                    productNameList.clear()
                                    productNameList.add("SELECT PRODUCT NAME")
                                    for(i in response.body()!!.indices) {
                                        productNameList.add(response.body()!![i].productName)
                                    }
                                    setProductNameSpinnerView()
                                }
                            }
                            override fun onFailure(call: Call<Array<Product>>, t: Throwable) {
                                productNameList.clear()
                                productNameList.add("SELECT PRODUCT NAME")
                                setProductNameSpinnerView()
                            }

                        })
                } else {
                    productNameList.clear()
                    productNameList.add("SELECT PRODUCT NAME")
                    setProductNameSpinnerView()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun setProductCategorySpinnerView() {
        activity_delete_product_productCategory.adapter = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            categoryList
        )
    }

    private fun setProductNameSpinnerView() {
        activity_delete_product_productName.adapter = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            productNameList
        )
    }
}