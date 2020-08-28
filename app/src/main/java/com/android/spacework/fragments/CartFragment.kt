package com.android.spacework.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.adapter.CartAdapter
import com.android.spacework.custom_helper.CustomDialog
import com.android.spacework.model.Cart
import com.android.spacework.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_cart.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartFragment : Fragment(), CustomDialog.CustomDialogListener {

    lateinit var sharedPreferences: SharedPreferences
    var totalPrice: Double = 0.0
    internal lateinit var view: View
    val arrayList: ArrayList<Cart> = arrayListOf()

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

        view!!.fragment_cart_next.setOnClickListener {
            val hashmapString = sharedPreferences.getString(
                Constants().USER_USERCART,
                ""
            )
            if(hashmapString == "" || hashmapString == "{}") {
                return@setOnClickListener
            }

            val customDialog = CustomDialog(this, activity!!, arrayList, totalPrice)
            customDialog.show()

        }

        return view
    }

    private fun getAllCartProducts() {
        arrayList.clear()
        var isNetworkError = false
        totalPrice = 0.0
        view!!.fragment_cart_progressSplash.visibility = View.VISIBLE
        view!!.fragment_cart_noItemInCartMessage.visibility = View.VISIBLE
        view!!.fragment_cart_itemExists.visibility = View.GONE
        view!!.fragment_cart_iconMessage.setImageResource(R.drawable.ic_sand_clock)
        view!!.fragment_cart_message.text = "Wait! We are loading your cart!"
        val hashmapString = sharedPreferences.getString(
            Constants().USER_USERCART,
            ""
        )
        Log.d("myCHECK", hashmapString)
        if(hashmapString == "" || hashmapString == "{}") {
            view!!.fragment_cart_itemExists.visibility = View.GONE
            view!!.fragment_cart_noItemInCartMessage.visibility = View.VISIBLE
            view!!.fragment_cart_iconMessage.setImageResource(R.drawable.ic_empty_white_box)
            view!!.fragment_cart_message.text = "There are no items in your cart!\nGo grab some!"
            view!!.fragment_cart_progressSplash.visibility = View.GONE
        } else {
            val gson = Gson()
            val type = object : TypeToken<HashMap<String?, ArrayList<String>?>?>() {}.type
            val hashmap: HashMap<String, ArrayList<String>> = gson.fromJson(hashmapString, type)
            val iterator = hashmap.entries.iterator()
            while(iterator.hasNext()) {
                val element = iterator.next() as Map.Entry<String,ArrayList<String>>
                val productAmount = element.value[0].toInt()
                val productName = element.value[1]
                val productPrice = element.value[2]
                val productImage = element.value[3]

                val cart = Cart(
                    productName,
                    productImage,
                    (productPrice.toDouble()*productAmount).toString(),
                    productAmount.toString()
                )
                totalPrice += (productPrice.toDouble()*productAmount)
                arrayList.add(cart)
            }
            Constants().progressAnimation(activity!!, view!!.fragment_cart_progressSplash)
            Handler().postDelayed({
                if(arrayList.isEmpty()) {
                    view!!.fragment_cart_noItemInCartMessage.visibility = View.VISIBLE
                    view!!.fragment_cart_itemExists.visibility = View.GONE
                    view!!.fragment_cart_progressSplash.visibility = View.GONE
                    if(isNetworkError) {
                        view!!.fragment_cart_iconMessage.setImageResource(R.drawable.ic_cancel)
                        view!!.fragment_cart_message.text = "Something's wrong!"
                    } else {
                        view!!.fragment_cart_iconMessage.setImageResource(R.drawable.ic_empty_white_box)
                        view!!.fragment_cart_message.text = "There are no items in your cart!\nGo grab some!"
                    }
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
            },Constants().delay)
        }
    }

    override fun onButtonClick() {
        val hashmapString = sharedPreferences.getString(
            Constants().USER_USERCART,
            ""
        )
        val userPhoneNumber = sharedPreferences.getString(
            Constants().USER_PHONENUMBER,
            ""
        )
        val userName = sharedPreferences.getString(
            Constants().USER_NAME,
            ""
        )
        val userAddress = sharedPreferences.getString(
            Constants().USER_ADDRESS,
            ""
        )
        val postOrderService = Constants().retrofit.create(API::class.java)
        postOrderService.postOrder(
            userPhoneNumber!!,
            userName!!,
            userAddress!!,
            hashmapString!!,
            totalPrice
        ).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    if(response.body().toString() == "Order Placed") {
                        Constants().generateSuccessToast(
                            activity!!,
                            context!!,
                            "Order Placed"
                        )
                        sharedPreferences.edit().putString(
                            Constants().USER_USERCART,
                            ""
                        ).apply()
                        getAllCartProducts()
                    }
                } else {
                    Constants().generateErrorToast(
                        activity!!,
                        context!!,
                        "Couldn't place order."
                    )
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Constants().generateErrorToast(
                    activity!!,
                    context!!,
                    "Couldn't connect to server. Try again later!"
                )
            }

        })
    }


}