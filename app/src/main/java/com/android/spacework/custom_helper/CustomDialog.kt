package com.android.spacework.custom_helper

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Window
import com.android.spacework.R
import com.android.spacework.model.Cart
import kotlinx.android.synthetic.main.custom_dialog.*

class CustomDialog(activity: Activity) : Dialog(activity) {

    interface CustomDialogListener {
        fun onButtonClick()
    }

    lateinit var mCustomDialogListener: CustomDialogListener
    lateinit var mList: ArrayList<Cart>
    var mTotalAmount: Double = 0.0

    constructor(mListener: CustomDialogListener, activity: Activity, list: ArrayList<Cart>, totalAmount: Double): this(activity) {
        this.mCustomDialogListener = mListener
        this.mList = list
        this.mTotalAmount = totalAmount
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)

        custom_dialog_productCount.text = mList.size.toString() + " items"
        var mySummary = ""
        for(i in 0..mList.lastIndex) {
            mySummary += "${i+1}) "+mList[i].productName + " - x"+ mList[i].productCount + "\n"
        }
        custom_dialog_productSummary.text = mySummary
        custom_dialog_orderAmount.text = "Please be present with ₹ $mTotalAmount at the time of delivery"

        custom_dialog_placeOrder.setOnClickListener {
            mCustomDialogListener.onButtonClick()
            dismiss()
        }
    }
}