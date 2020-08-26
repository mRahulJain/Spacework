package com.android.spacework.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.Helper.ProgressBarAnim
import com.android.spacework.R
import com.android.spacework.adapter.CartAdapter
import com.android.spacework.model.Cart
import com.android.spacework.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    var totalPrice: Double = 0.0
    internal lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_cart, container, false)
        sharedPreferences = activity!!.getSharedPreferences(
            Constants().USER_INFO_PREFERENCE,
            Context.MODE_PRIVATE
        )
        getAllCartProducts()

        view!!.fragment_cart_refresh.setOnRefreshListener {
            getAllCartProducts()
            view!!.fragment_cart_refresh.isRefreshing = false
        }

        return view
    }

    private fun getAllCartProducts() {
        totalPrice = 0.0
        view!!.fragment_cart_noItemInCartMessage.visibility = View.VISIBLE
        view!!.fragment_cart_itemExists.visibility = View.GONE
        view!!.fragment_cart_iconMessage.setImageResource(R.drawable.ic_sand_clock)
        view!!.fragment_cart_message.text = "Wait! We are loading your cart!"
        val hashmapString = sharedPreferences.getString(
            Constants().USER_USERCART,
            ""
        )
        if(hashmapString == "" || hashmapString == "{}") {
            view!!.fragment_cart_itemExists.visibility = View.GONE
            view!!.fragment_cart_noItemInCartMessage.visibility = View.VISIBLE
            view!!.fragment_cart_iconMessage.setImageResource(R.drawable.ic_empty_white_box)
            view!!.fragment_cart_message.text = "There are no items in your cart!\nGo grab some!"
            view!!.fragment_cart_progressSplash.visibility = View.GONE
        } else {
            val gson = Gson()
            val type = object : TypeToken<HashMap<String?, String?>?>() {}.type
            val hashmap: HashMap<String, String> = gson.fromJson(hashmapString, type)
            val arrayList: ArrayList<Cart> = arrayListOf()
            val iterator = hashmap.entries.iterator()
            while(iterator.hasNext()) {
                val element = iterator.next() as Map.Entry<String,String>
                val productId = element.key
                val productAmount = element.value.toInt()
                val getProductByIdService = Constants().retrofit.create(API::class.java)
                getProductByIdService.getProductById(productId).enqueue(object : Callback<Product>{
                    override fun onResponse(call: Call<Product>, response: Response<Product>) {
                        if(response.isSuccessful) {
                            val cart = Cart(
                                response.body()!!.productName,
                                response.body()!!.productImage,
                                (response.body()!!.productPrice.toDouble()*productAmount).toString(),
                                productAmount.toString()
                            )
                            totalPrice += (response.body()!!.productPrice.toDouble()*productAmount)
                            arrayList.add(cart)
                        }
                    }
                    override fun onFailure(call: Call<Product>, t: Throwable) {
                        Constants().generateErrorToast(
                            activity!!,
                            context!!,
                            t.localizedMessage
                        )
                    }
                })
            }
            progressAnimation()
            Handler().postDelayed({
                if(arrayList.isEmpty()) {
                    view!!.fragment_cart_noItemInCartMessage.visibility = View.VISIBLE
                    view!!.fragment_cart_itemExists.visibility = View.GONE
                    view!!.fragment_cart_iconMessage.setImageResource(R.drawable.ic_empty_white_box)
                    view!!.fragment_cart_message.text = "There are no items in your cart!\nGo grab some!"
                    view!!.fragment_cart_progressSplash.visibility = View.GONE
                } else {
                    view!!.fragment_cart_recyclerView.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    view!!.fragment_cart_recyclerView.adapter = CartAdapter(activity!!, context!!, arrayList)
                    view!!.fragment_cart_totalPrice.text = "₹ $totalPrice"
                    view!!.fragment_cart_noItemInCartMessage.visibility = View.GONE
                    view!!.fragment_cart_itemExists.visibility = View.VISIBLE
                }
            },8000)
        }
    }

    fun progressAnimation() {
        val anim = ProgressBarAnim(activity!!, view!!.fragment_cart_progressSplash, 0f, 100f)
        anim.duration = 8000
        view!!.fragment_cart_progressSplash.animation = anim
    }
}