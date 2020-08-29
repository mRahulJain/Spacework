package com.android.spacework.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.activity.admin.AdminViewOrderActivity
import com.android.spacework.model.Order
import com.android.spacework.model.Orders
import kotlinx.android.synthetic.main.fragment_pending_orders.view.*
import kotlinx.android.synthetic.main.list_item_admin_orders.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Handler

class AdminOrderAdapter(val activity: Activity, val context: Context, val orderList: Array<Orders>)
    : RecyclerView.Adapter<AdminOrderAdapter.NameViewHolder>() {

    var width = 0
    val displayMetrics = DisplayMetrics()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.list_item_admin_orders, parent, false)

        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels

        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.itemView.list_item_admin_orders_parent.layoutParams.width = width/2
        holder.itemView.list_item_admin_orders_username.text = orderList[position]!!.userName

        for(i in 0..orderList[position]!!.userOrders.lastIndex) {
            if(orderList[position]!!.userOrders[i]!!.orderStatus == "Order Received") {
                holder.itemView.list_item_admin_orders_parent.setBackgroundColor(Color.parseColor("#90ee90"))
                break
            }
        }

        holder.itemView.list_item_admin_orders_parent.setOnClickListener {
            val intent = Intent(activity, AdminViewOrderActivity::class.java)
            intent.putExtra("userPhoneNumber", orderList[position]!!.userPhoneNumber)
            intent.putExtra("userName", orderList[position]!!.userName)
            intent.putExtra("userAddress", orderList[position]!!.userAddress)
            activity.startActivity(intent)
        }
    }

    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}