package com.android.spacework.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.model.Product
import kotlinx.android.synthetic.main.activity_update_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast

class UpdateProductActivity : AppCompatActivity() {

    private val categoryList = arrayListOf(
        "SELECT PRODUCT CATEGORY", "Water", "Cold Drink"
    )

    private var productNameList = arrayListOf(
        "SELECT PRODUCT NAME"
    )

    private var hashmap: HashMap<String, Product> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_product)
        setProductCategorySpinnerView()
        setProductNameSpinnerView()

        activity_update_product_productCategory.onItemSelectedListener = spinnerProductCategoryListener()
        activity_update_product_productName.onItemSelectedListener = spinnerProductNameListener()

        activity_update_product_update.setOnClickListener {
            if(activity_update_product_productCategory.selectedItem.toString() == "SELECT PRODUCT CATEGORY" ||
                activity_update_product_productName.selectedItem.toString() == "SELECT PRODUCT NAME") {
                Constants().generateErrorToast(this, this, "No item selected")
                return@setOnClickListener
            }
            updateProduct()
        }
    }

    private fun updateProduct() {
        activity_update_product_update.startAnimation()
        val updateProductService = Constants().retrofit.create(API::class.java)
        updateProductService.updateProduct(
            activity_update_product_productName.selectedItem.toString(),
            activity_update_product_description.text.toString(),
            activity_update_product_price.text.toString().toDouble(),
            activity_update_product_isAvailable.isChecked,
            activity_update_product_imageLink.text.toString()
        ).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    Constants().generateSuccessToast(
                        this@UpdateProductActivity,
                        this@UpdateProductActivity,
                        response.body().toString())
                    activity_update_product_description.setText("")
                    activity_update_product_price.setText("")
                    activity_update_product_imageLink.setText("")
                    activity_update_product_isAvailable.isChecked = false
                    productNameList.clear()
                    hashmap.clear()
                    productNameList.add("SELECT PRODUCT NAME")
                    setProductNameSpinnerView()
                    setProductCategorySpinnerView()
                } else {
                    Log.e("myERROR", response.errorBody().toString())
                }
                activity_update_product_update.revertAnimation()
                activity_update_product_update.setBackgroundResource(R.drawable.blue_fill__rounded_color)
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("myERROR", t.localizedMessage)
                activity_update_product_update.revertAnimation()
                activity_update_product_update.setBackgroundResource(R.drawable.blue_fill__rounded_color)
            }
        })
    }

    private fun setProductCategorySpinnerView() {
        activity_update_product_productCategory.adapter = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            categoryList
        )
    }

    private fun setProductNameSpinnerView() {
        activity_update_product_productName.adapter = ArrayAdapter<String>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            productNameList
        )
    }

    private fun spinnerProductNameListener(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(hashmap.containsKey(p0!!.selectedItem.toString())) {
                    val product = hashmap[p0!!.selectedItem.toString()]
                    activity_update_product_description.setText(product!!.productDescription)
                    activity_update_product_price.setText(product!!.productPrice)
                    activity_update_product_imageLink.setText(product!!.productImage)
                    activity_update_product_isAvailable.isChecked = product!!.productIsAvailable == "true"
                } else {
                    activity_update_product_description.setText("")
                    activity_update_product_price.setText("")
                    activity_update_product_imageLink.setText("")
                    activity_update_product_isAvailable.isChecked = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun spinnerProductCategoryListener() : AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p0!!.selectedItem.toString() != "SELECT PRODUCT CATEGORY") {
                    val getProductByCategoryService = Constants().retrofit.create(API::class.java)
                    getProductByCategoryService.getProductByCategory(p0!!.selectedItem.toString())
                        .enqueue(object : Callback<Array<Product>>{
                            override fun onResponse(
                                call: Call<Array<Product>>,
                                response: Response<Array<Product>>
                            ) {
                                if(response!!.isSuccessful) {
                                    hashmap.clear()
                                    productNameList.clear()
                                    productNameList.add("SELECT PRODUCT NAME")
                                    for(i in response.body()!!.indices) {
                                        hashmap[response.body()!![i].productName] = response.body()!![i]
                                        productNameList.add(response.body()!![i].productName)
                                    }
                                    setProductNameSpinnerView()
                                }
                            }
                            override fun onFailure(call: Call<Array<Product>>, t: Throwable) {
                                productNameList.clear()
                                hashmap.clear()
                                productNameList.add("SELECT PRODUCT NAME")
                                setProductNameSpinnerView()
                            }

                        })
                } else {
                    productNameList.clear()
                    hashmap.clear()
                    productNameList.add("SELECT PRODUCT NAME")
                    setProductNameSpinnerView()
                }
                activity_update_product_description.setText("")
                activity_update_product_price.setText("")
                activity_update_product_imageLink.setText("")
                activity_update_product_isAvailable.isChecked = false
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}