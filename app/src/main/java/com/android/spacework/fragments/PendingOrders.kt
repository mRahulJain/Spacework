package com.android.spacework.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.activity.admin.AdminViewOrderActivity
import com.android.spacework.adapter.AdminViewOrderAdapter
import com.android.spacework.model.Order
import kotlinx.android.synthetic.main.fragment_pending_orders.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingOrders : Fragment() {

    internal lateinit var view: View
    private lateinit var userPhoneNumber: String
    private lateinit var mActivity: AdminViewOrderActivity
    var mArrayList: ArrayList<Order> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_pending_orders, container, false)
        mActivity = activity!! as AdminViewOrderActivity
        userPhoneNumber = mActivity!!.getUserPhoneNumber()
        userPhoneNumber = userPhoneNumber!!.substring(1, userPhoneNumber!!.length)
        getUserOrderDetails()

        view!!.fragment_pending_orders_refresh.setOnRefreshListener {
            getUserOrderDetails()
            view!!.fragment_pending_orders_refresh.isRefreshing = false
        }

        return view
    }

    private fun getUserOrderDetails() {
        view!!.fragment_pending_orders_progressBar.visibility = View.VISIBLE
        view!!.fragment_pending_orders_recyclerView.visibility = View.GONE
        val getUserOrderService = Constants().retrofit.create(API::class.java)
        getUserOrderService.getOrdersByID(userPhoneNumber).enqueue(object : Callback<Array<Order>>{
            override fun onResponse(call: Call<Array<Order>>, response: Response<Array<Order>>) {
                if(response.isSuccessful) {
                    mArrayList.clear()
                    val arr =  response.body()!!
                    for(i in arr.lastIndex downTo 0) {
                        if(arr[i]!!.orderStatus == "Order Received" || arr[i]!!.orderHasPaid == "no") {
                            mArrayList.add(arr[i])
                        }
                    }

                    if(mArrayList.isEmpty()) {
                        view!!.fragment_pending_orders_progressBar.visibility = View.GONE
                        view!!.fragment_pending_orders_recyclerView.visibility = View.GONE
                        view!!.fragment_pending_orders_emptyListMessage.visibility = View.VISIBLE
                    } else {
                        view!!.fragment_pending_orders_progressBar.visibility = View.GONE
                        view!!.fragment_pending_orders_recyclerView.visibility = View.VISIBLE
                        view!!.fragment_pending_orders_emptyListMessage.visibility = View.GONE

                        view!!.fragment_pending_orders_recyclerView.layoutManager = LinearLayoutManager(
                            mActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        view!!.fragment_pending_orders_recyclerView.adapter = AdminViewOrderAdapter(mActivity, mActivity, userPhoneNumber, mArrayList)
                    }
                } else {
                    view!!.fragment_pending_orders_progressBar.visibility = View.GONE
                    view!!.fragment_pending_orders_recyclerView.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<Array<Order>>, t: Throwable) {
                view!!.fragment_pending_orders_progressBar.visibility = View.GONE
                view!!.fragment_pending_orders_recyclerView.visibility = View.GONE
            }

        })
    }
}