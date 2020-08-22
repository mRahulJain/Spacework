package com.android.spacework.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import com.android.spacework.R
import com.android.spacework.adapter.TabAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_app.*

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        val tabAdapter = TabAdapter(this.supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.activity_app_viewPager)
        viewPager.adapter = tabAdapter
        val tabs: TabLayout = findViewById(R.id.activity_app_tabLayout)
        tabs.setupWithViewPager(viewPager, true)
        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_home)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_cart)

        val layout = tabs.getChildAt(0) as LinearLayout
        layout.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        val drawable = GradientDrawable()
        drawable.setColor(Color.parseColor("#80FFFFFF"))
        drawable.setSize(3,0)
        layout.dividerPadding = 25
        layout.dividerDrawable = drawable

        activity_app_adminZone.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

    }
}