package com.android.spacework.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.spacework.R
import com.android.spacework.model.Cart
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_cart.view.*


class CartAdapter(val activity: Activity, val context: Context, val productList: ArrayList<Cart>)
    : RecyclerView.Adapter<CartAdapter.NameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.list_item_cart, parent, false)
        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        val price = (productList[position]!!.productTotalPrice).toDouble()/(productList[position]!!.productCount).toDouble()
        Picasso.with(context)
            .load(productList[position]!!.productImage)
            .into(holder.itemView.list_item_cart_productImage)
        holder.itemView.list_item_cart_productName.text = productList[position]!!.productName
        holder.itemView.list_item_cart_productPriceTotal.text = "₹ $price"
        holder.itemView.list_item_cart_itemCount.text = "x"+productList[position]!!.productCount
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}