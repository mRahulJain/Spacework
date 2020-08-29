package com.android.spacework.activity.admin

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.android.spacework.R
import com.android.spacework.adapter.AdminTabAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_admin_view_order.*

class AdminViewOrderActivity : AppCompatActivity() {

    private var userName: String = ""
    private var userPhoneNumber: String = ""
    private var userAddress: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_order)

        userName = intent.getStringExtra("userName")
        userPhoneNumber = intent.getStringExtra("userPhoneNumber")
        userAddress = intent.getStringExtra("userAddress")

        activity_admin_view_order_address.text = "Address - $userAddress"

        activity_admin_view_order_toolbarText.text = "Spacework - $userName"

        val tabAdapter = AdminTabAdapter(this.supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.activity_admin_view_order_viewPager)
        viewPager.adapter = tabAdapter
        val tabs: TabLayout = findViewById(R.id.activity_admin_view_order_tabLayout)
        tabs.setupWithViewPager(viewPager, true)

        val layout = tabs.getChildAt(0) as LinearLayout
        layout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor("#80FFFFFF"))
        drawable.setSize(3, 0)
        layout.dividerPadding = 25
        layout.dividerDrawable = drawable
    }

    fun getUserPhoneNumber(): String {
        return userPhoneNumber
    }
}