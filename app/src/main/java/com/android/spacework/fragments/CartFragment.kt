package com.android.spacework.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.adapter.CartAdapter
import com.android.spacework.adapter.HomeAdapter
import com.android.spacework.model.Product
import kotlinx.android.synthetic.main.fragment_cart.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        getAllProducts(view!!)

        return view
    }

    private fun getAllProducts(view: View) {
        val getAllProductsService = Constants().retrofit.create(API::class.java)
        getAllProductsService.getAllProducts().enqueue(object : Callback<Array<Product>> {
            override fun onResponse(
                call: Call<Array<Product>>,
                response: Response<Array<Product>>
            ) {
                if(response.isSuccessful) {
                    val listAvailableProduct = arrayListOf<Product>()
                    val listUnavailableProduct = arrayListOf<Product>()

                    for(i in 0..response.body()!!.lastIndex) {
                        if(response.body()!![i].productIsAvailable == "true") {
                            listAvailableProduct.add(response.body()!![i])
                        } else {
                            listUnavailableProduct.add(response.body()!![i])
                        }
                    }
                    val finalList = arrayListOf<Product>()
                    finalList.addAll(listAvailableProduct)
//                    finalList.addAll(listUnavailableProduct)
                    view!!.fragment_cart_recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    view!!.fragment_cart_recyclerView.adapter = CartAdapter(activity!!, context!!, finalList)
                } else {
                    Log.e("myERROR", response.errorBody().toString())
                }
            }
            override fun onFailure(call: Call<Array<Product>>, t: Throwable) {
                Log.e("myERROR", t.localizedMessage)
            }
        })
    }
}