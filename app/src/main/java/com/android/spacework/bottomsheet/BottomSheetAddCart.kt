package com.android.spacework.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.spacework.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_add_cart.*
import kotlinx.android.synthetic.main.bottom_sheet_add_cart.view.*

class BottomSheetAddCart() : BottomSheetDialogFragment() {

    interface BottomSheetAddCartListener {
        fun onButtonClick(flag: Boolean)
    }

    var count = 0
    lateinit var mListener: BottomSheetAddCartListener

    constructor(
        mListener: BottomSheetAddCartListener,
        mCount: Int
    ) : this() {
        this.mListener = mListener
        this.count = mCount
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_add_cart, container, false)

        view!!.bottom_sheet_add_cart_count.text = count.toString()

        view!!.bottom_sheet_add_cart_increase.setOnClickListener {
            count++
            mListener.onButtonClick(true)
            view!!.bottom_sheet_add_cart_count.text = count.toString()
        }
        view!!.bottom_sheet_add_cart_decrease.setOnClickListener {
            if(count == 0) {
                return@setOnClickListener
            }
            count--
            mListener.onButtonClick(false)
            view!!.bottom_sheet_add_cart_count.text = count.toString()
        }

        return view
    }

}