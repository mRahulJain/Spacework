package com.android.spacework.activity.user

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.android.spacework.Helper.Constants
import com.android.spacework.R
import com.android.spacework.activity.admin.*
import com.android.spacework.activity.login.LoginActivity
import com.android.spacework.adapter.TabAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_app.*

class AppActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        sharedPreferences = this.getSharedPreferences(
            Constants().USER_INFO_PREFERENCE,
            MODE_PRIVATE
        )

        getUserType()
        checkUserLoggedIn()

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
        drawable.setSize(3, 0)
        layout.dividerPadding = 25
        layout.dividerDrawable = drawable

        val dropDownMenu = PopupMenu(this, activity_app_adminZone)
        val menu = dropDownMenu.menu
        dropDownMenu.menuInflater.inflate(R.menu.admin_menu, menu)

        dropDownMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.tab_viewOrders ->{
                    val intent = Intent(this, AdminOrderActivity::class.java)
                    startActivity(intent)
                    return@OnMenuItemClickListener true
                }
                R.id.tab_addProduct -> {
                    val intent = Intent(this, AddProductActivity::class.java)
                    startActivity(intent)
                    return@OnMenuItemClickListener true
                }
                R.id.tab_updateProduct -> {
                    val intent = Intent(this, UpdateProductActivity::class.java)
                    startActivity(intent)
                    return@OnMenuItemClickListener true
                }
                R.id.tab_deleteProduct -> {
                    val intent = Intent(this, DeleteProductActivity::class.java)
                    startActivity(intent)
                    return@OnMenuItemClickListener true
                }
            }
            false
        })

        activity_app_adminZone.setOnClickListener {
            dropDownMenu.show()
        }

        activity_app_profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        activity_app_orders.setOnClickListener {
            val intent = Intent(this, BagActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getUserType() {
        val userType = sharedPreferences.getString(Constants().USER_USERTYPE, "")
        if(userType == "admin") {
            activity_app_adminZone.visibility = View.VISIBLE
        } else {
            activity_app_adminZone.visibility = View.GONE
        }
    }

    private fun checkUserLoggedIn() {
        val userName = sharedPreferences.getString(Constants().USER_NAME, "")
        if(userName == "") {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}