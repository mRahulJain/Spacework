package com.android.spacework.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.spacework.fragments.CompletedOrders
import com.android.spacework.fragments.PendingOrders

class AdminTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        lateinit var returnFragment : Fragment

        when (position) {
            0 -> {
                returnFragment = PendingOrders()
            }
            1 -> {
                returnFragment = CompletedOrders()
            }
        }
        return returnFragment
    }

    override fun getCount(): Int {
        return 2
    }


    override fun getPageTitle(position: Int): CharSequence? {
        var ch : CharSequence? = null
        when (position) {
            0 -> {
                ch = "Pending Orders"
            }
            1 -> {
                ch = "Completed Orders"
            }
        }
        return ch
    }

}