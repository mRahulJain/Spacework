package com.android.spacework.adapter

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.spacework.R
import com.android.spacework.model.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_product.view.*


class HomeAdapter(val activity: Activity, val context: Context, val productList: ArrayList<Product>)
    : RecyclerView.Adapter<HomeAdapter.NameViewHolder>() {

    val displayMetrics = DisplayMetrics()
    var width = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = li.inflate(R.layout.list_item_product, parent, false)

        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels

        return NameViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.itemView.list_item_product_productImage.layoutParams.width = width/2

        holder.itemView.list_item_product_productName.text = productList[position].productName
        holder.itemView.list_item_product_productDescription.text = productList[position].productDescription
        holder.itemView.list_item_product_productPrice.text = "₹ "+productList[position].productPrice
        if(productList[position].productIsAvailable == "false") {
            holder.itemView.list_item_product_myProduct.alpha = 0.2f
            holder.itemView.list_item_product_isAvailable.visibility = View.VISIBLE
        } else {
            holder.itemView.list_item_product_myProduct.alpha = 1.0f
            holder.itemView.list_item_product_isAvailable.visibility = View.GONE
        }

        Picasso.with(context)
            .load(productList[position].productImage)
            .into(holder.itemView.list_item_product_productImage)
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}