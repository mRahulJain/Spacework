package com.android.spacework.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.adapter.BagAdapter
import com.android.spacework.model.Order
import kotlinx.android.synthetic.main.activity_bag.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BagActivity : AppCompatActivity() {

    var mArrayList: ArrayList<Order> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bag)

        activity_bag_progressBar.visibility = View.VISIBLE
        activity_bag_noOrders.visibility = View.GONE

        val getOrderService = Constants().retrofit.create(API::class.java)
        val sharedPreferences = this.getSharedPreferences(
            Constants().USER_INFO_PREFERENCE,
            MODE_PRIVATE
        )
        var phoneNumber = sharedPreferences.getString(
            Constants().USER_PHONENUMBER,
            ""
        )
        phoneNumber = phoneNumber!!.substring(1, phoneNumber!!.length)
        getOrderService.getOrdersByID(phoneNumber).enqueue(object : Callback<Array<Order>>{
            override fun onResponse(call: Call<Array<Order>>, response: Response<Array<Order>>) {
                if(response.isSuccessful) {
                    mArrayList.clear()
                    val arr =  response.body()!!
                    for(i in arr.lastIndex downTo 0) {
                        mArrayList.add(arr[i])
                    }

                    activity_bag_progressBar.visibility = View.GONE
                    activity_bag_recyclerView.visibility = View.VISIBLE
                    activity_bag_noOrders.visibility = View.GONE
                    activity_bag_recyclerView.layoutManager = LinearLayoutManager(
                        this@BagActivity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    activity_bag_recyclerView.adapter = BagAdapter(this@BagActivity, this@BagActivity, phoneNumber, mArrayList)
                }
            }

            override fun onFailure(call: Call<Array<Order>>, t: Throwable) {
                activity_bag_noOrders.visibility = View.VISIBLE
                activity_bag_progressBar.visibility = View.GONE
                activity_bag_recyclerView.visibility = View.GONE
            }

        })

    }
}