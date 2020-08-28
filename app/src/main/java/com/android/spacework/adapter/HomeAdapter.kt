package com.android.spacework.adapter

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.custom_helper.CustomBottomSheet
import com.android.spacework.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_product.view.*


class HomeAdapter(val activity: Activity, val context: Context, val productList: ArrayList<Product>)
    : RecyclerView.Adapter<HomeAdapter.NameViewHolder>(), CustomBottomSheet.BottomSheetAddCartListener {

    val displayMetrics = DisplayMetrics()
    var width = 0
    var selectedItem = ""
    var itemList =  arrayListOf<String>()

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

        holder.itemView.list_item_product_productAddToCard.setOnClickListener {
            itemList.clear()
            if(productList[position].productIsAvailable == "false") {
                return@setOnClickListener
            }
            selectedItem = productList[position]!!.productId
            itemList.add(productList[position]!!.productName)
            itemList.add(productList[position]!!.productPrice)
            itemList.add(productList[position]!!.productImage)

            val c = getCountIfPresent(selectedItem)
            val bottomSheet = CustomBottomSheet(this, c)
            bottomSheet.show((context as AppCompatActivity).supportFragmentManager, "bottom sheet")
        }

        Picasso.with(context)
            .load(productList[position].productImage)
            .into(holder.itemView.list_item_product_productImage)
    }


    class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onButtonClick(flag: Boolean) {
        val sharedPreferences = context.getSharedPreferences(
            Constants().USER_INFO_PREFERENCE,
            Context.MODE_PRIVATE
        )
        val hashmapString = sharedPreferences.getString(Constants().USER_USERCART, "")
        if(hashmapString == "" || hashmapString == "{}") {
            val hashmap = HashMap<String, ArrayList<String>>()
            itemList.add(0, "1")
            hashmap[selectedItem] = itemList
            val gson = Gson()
            val hashmapString = gson.toJson(hashmap)
            sharedPreferences.edit().putString(
                Constants().USER_USERCART,
                hashmapString
            ).apply()
        } else {
            val gson = Gson()
            val type = object : TypeToken<HashMap<String?, ArrayList<String>?>?>() {}.type
            val hashmap: HashMap<String, ArrayList<String>> = gson.fromJson(hashmapString, type)
            if(hashmap.containsKey(selectedItem)) {
                var count = hashmap[selectedItem]!![0].toInt()
                if(flag) {
                    count++
                } else {
                    count--
                }
                if(count == 0) {
                    hashmap.remove(selectedItem)
                } else {
                    hashmap[selectedItem]!!.set(0, count.toString())
                }
            } else {
                itemList.add(0, "1")
                hashmap[selectedItem] = itemList
            }
            val hashmapString = gson.toJson(hashmap)
            sharedPreferences.edit().putString(
                Constants().USER_USERCART,
                hashmapString
            ).apply()
        }
    }

    private fun getCountIfPresent(productId: String): Int {
        val sharedPreferences = context.getSharedPreferences(
            Constants().USER_INFO_PREFERENCE,
            Context.MODE_PRIVATE
        )
        val hashmapString = sharedPreferences.getString(Constants().USER_USERCART, "")
        return if(hashmapString == "") {
            0
        } else {
            val gson = Gson()
            val type = object : TypeToken<HashMap<String?, ArrayList<String>?>?>() {}.type
            val hashmap: HashMap<String, ArrayList<String>> = gson.fromJson(hashmapString, type)
            if(hashmap.containsKey(productId)) {
                hashmap[productId]!![0].toInt()
            } else {
                0
            }
        }
    }
}