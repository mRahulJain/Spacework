package com.android.spacework.model

data class Order(
    val orderHashmap: String = "",
    val orderStatus: String = "",
    val orderDate: String = "",
    val orderTotal: String = "",
    val orderHasPaid: String = ""
)

data class Orders(
    val userPhoneNumber: String = "",
    val userName: String = "",
    val userAddress: String = "",
    val userOrders: Array<Order> = arrayOf()
)