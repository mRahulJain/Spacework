package com.android.spacework.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.adapter.AdminOrderAdapter
import com.android.spacework.model.Orders
import kotlinx.android.synthetic.main.activity_admin_order.*
import kotlinx.android.synthetic.main.fragment_pending_orders.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_order)

        getOrders()

        activity_admin_order_refresh.setOnRefreshListener {
            activity_admin_order_refresh.isRefreshing = false
            getOrders()
        }
    }

    private fun getOrders() {
        activity_admin_order_progressBar.visibility = View.VISIBLE
        activity_admin_order_recyclerView.visibility = View.GONE

        val getAllOrdersService = Constants().retrofit.create(API::class.java)
        getAllOrdersService.getAllOrders().enqueue(object : Callback<Array<Orders>> {
            override fun onResponse(call: Call<Array<Orders>>, response: Response<Array<Orders>>) {
                if(response.isSuccessful) {
                    activity_admin_order_progressBar.visibility = View.GONE
                    activity_admin_order_recyclerView.visibility = View.VISIBLE
                    activity_admin_order_recyclerView.layoutManager = GridLayoutManager(
                        this@AdminOrderActivity,
                        2,
                        GridLayoutManager.VERTICAL,
                        false
                    )
                    activity_admin_order_recyclerView.adapter = AdminOrderAdapter(
                        this@AdminOrderActivity,
                        this@AdminOrderActivity,
                        response.body()!!
                    )
                }
            }
            override fun onFailure(call: Call<Array<Orders>>, t: Throwable) {
                activity_admin_order_progressBar.visibility = View.GONE
                activity_admin_order_recyclerView.visibility = View.GONE
            }
        })
    }
}