package com.android.spacework.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.spacework.API.API
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.model.Order
import com.android.spacework.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.list_item_order.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler

class BagAdapter(val activity: Activity, val context: Context, val phoneNumber: String,val orderList: ArrayList<Order>)
    : RecyclerView.Adapter<BagAdapter.NameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.list_item_order, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        val date = orderList[position]!!.orderDate
        val actualDate = date.substring(0,10)
        holder.itemView.list_item_order_date.text = "$actualDate"
        holder.itemView.list_item_order_orderTotal.text = "₹ "+orderList[position]!!.orderTotal
        holder.itemView.list_item_order_orderStatus.text = orderList[position]!!.orderStatus
        if(orderList[position]!!.orderStatus == "Order Received") {
            holder.itemView.list_item_cart_cancelOrder.visibility = View.VISIBLE
        }
        var orderDetails = ""
        val hashmapString = orderList[position]!!.orderHashmap
        val gson = Gson()
        val type = object : TypeToken<HashMap<String?, ArrayList<String>?>?>() {}.type
        val hashmap: HashMap<String, ArrayList<String>> = gson.fromJson(hashmapString, type)
        val iterator = hashmap.entries.iterator()
        while(iterator.hasNext()) {
            val element = iterator.next() as Map.Entry<String, ArrayList<String>>
            val productAmount = element.value[0].toInt()
            val productName = element.value[1]
            orderDetails += "$productName - x$productAmount\n"
        }
        orderDetails = orderDetails.trim()
        holder.itemView.list_item_order_orderDetails.text = orderDetails

        holder.itemView.list_item_cart_cancelOrder.setOnClickListener {

            val cancelOrderService = Constants().retrofit.create(API::class.java)
            cancelOrderService.cancelOrder(phoneNumber, orderList[position]!!.orderHashmap)
                .enqueue(object : Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if(response.isSuccessful) {
                            if(response.body() == "Ordered Cancelled") {
                                Constants().generateSuccessToast(
                                    activity!!,
                                    context!!,
                                    "Order Cancelled successfully"
                                )
                                activity!!.finish()
                            }
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                    }

                })
        }
    }

    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}