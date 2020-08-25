package com.android.spacework.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.adapter.HomeAdapter
import com.android.spacework.model.Product
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var myCurrentType: String = "All Items"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        getAllProducts(view)

        view!!.fragment_home_allProducts.setOnClickListener {
            if(myCurrentType == "All Items") {
                return@setOnClickListener
            }
            myCurrentType = "All Items"
            setBackgroundForFilters(
                fragment_home_allProducts,
                fragment_home_water,
                fragment_home_coldDrink
            )
            getAllProducts(view!!)
        }

        view!!.fragment_home_water.setOnClickListener {
            if(myCurrentType == "Water") {
                return@setOnClickListener
            }
            myCurrentType = "Water"
            setBackgroundForFilters(
                fragment_home_water,
                fragment_home_allProducts,
                fragment_home_coldDrink
            )
            getProductsByCategory(view!!, "Water")
        }

        view!!.fragment_home_coldDrink.setOnClickListener {
            if(myCurrentType == "Cold Drink") {
                return@setOnClickListener
            }
            myCurrentType = "Cold Drink"
            setBackgroundForFilters(
                fragment_home_coldDrink,
                fragment_home_water,
                fragment_home_allProducts
            )
            getProductsByCategory(view!!, "Cold Drink")
        }

        return view
    }

    private fun setBackgroundForFilters(a: TextView, b: TextView, c: TextView) {
        a.setTextColor(Color.parseColor("#1566e0"))
        a.setBackgroundResource(R.drawable.blue_border_rounded_cornwe)
        b.setTextColor(Color.parseColor("#aaaaaa"))
        b.setBackgroundResource(R.drawable.gray_border_rounded_corners)
        c.setTextColor(Color.parseColor("#aaaaaa"))
        c.setBackgroundResource(R.drawable.gray_border_rounded_corners)
    }

    private fun getProductsByCategory(view: View, productCategory: String) {
        view!!.fragment_home_loader.visibility = View.VISIBLE
        view!!.fragment_home_recyclerView.visibility = View.GONE
        view!!.fragment_home_errorMessage.visibility = View.GONE
        val getAllProductsService = Constants().retrofit.create(API::class.java)
        getAllProductsService.getProductByCategory(productCategory).enqueue(object : Callback<Array<Product>>{
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
                    finalList.addAll(listUnavailableProduct)
                    view!!.fragment_home_recyclerView.layoutManager = GridLayoutManager(context, 2,GridLayoutManager.VERTICAL, false)
                    view!!.fragment_home_recyclerView.adapter = HomeAdapter(activity!!, context!!, finalList)
                    view!!.fragment_home_loader.visibility = View.GONE
                    view!!.fragment_home_recyclerView.visibility = View.VISIBLE
                    view!!.fragment_home_errorMessage.visibility = View.GONE
                } else {
                    Log.e("myERROR", response.errorBody().toString())
                    view!!.fragment_home_loader.visibility = View.GONE
                    view!!.fragment_home_recyclerView.visibility = View.GONE
                    view!!.fragment_home_errorMessage.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<Array<Product>>, t: Throwable) {
                Log.e("myERROR", t.localizedMessage)
                view!!.fragment_home_loader.visibility = View.GONE
                view!!.fragment_home_recyclerView.visibility = View.GONE
                view!!.fragment_home_errorMessage.visibility = View.VISIBLE
            }
        })
    }

    private fun getAllProducts(view: View) {
        view!!.fragment_home_loader.visibility = View.VISIBLE
        view!!.fragment_home_recyclerView.visibility = View.GONE
        view!!.fragment_home_errorMessage.visibility = View.GONE
        val getAllProductsService = Constants().retrofit.create(API::class.java)
        getAllProductsService.getAllProducts().enqueue(object : Callback<Array<Product>>{
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
                    finalList.addAll(listUnavailableProduct)
                    view!!.fragment_home_recyclerView.layoutManager = GridLayoutManager(context, 2,GridLayoutManager.VERTICAL, false)
                    view!!.fragment_home_recyclerView.adapter = HomeAdapter(activity!!, context!!, finalList)
                    view!!.fragment_home_loader.visibility = View.GONE
                    view!!.fragment_home_recyclerView.visibility = View.VISIBLE
                    view!!.fragment_home_errorMessage.visibility = View.GONE
                } else {
                    Log.e("myERROR", response.errorBody().toString())
                    view!!.fragment_home_loader.visibility = View.GONE
                    view!!.fragment_home_recyclerView.visibility = View.GONE
                    view!!.fragment_home_errorMessage.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<Array<Product>>, t: Throwable) {
                Log.e("myERROR", t.localizedMessage)
                view!!.fragment_home_loader.visibility = View.GONE
                view!!.fragment_home_recyclerView.visibility = View.GONE
                view!!.fragment_home_errorMessage.visibility = View.VISIBLE
            }
        })
    }
}