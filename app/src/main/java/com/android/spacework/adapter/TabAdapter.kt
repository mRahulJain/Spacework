package com.android.spacework.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.spacework.fragments.CartFragment
import com.android.spacework.fragments.HomeFragment

class TabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        lateinit var returnFragment : Fragment

        when (position) {
            0 -> {
                returnFragment = HomeFragment()
            }
            1 -> {
                returnFragment = CartFragment()
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
                ch = "HOME"
            }
            1 -> {
                ch = "CART"
            }
        }
        return ch
    }

}