package com.android.spacework.activity.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.spacework.R
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        activity_admin_addProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }

        activity_admin_updateProduct.setOnClickListener {
            val intent = Intent(this, UpdateProductActivity::class.java)
            startActivity(intent)
        }

        activity_admin_deleteProduct.setOnClickListener {
            val intent = Intent(this, DeleteProductActivity::class.java)
            startActivity(intent)
        }
    }
}