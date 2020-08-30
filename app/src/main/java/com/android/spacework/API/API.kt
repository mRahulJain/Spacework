package com.android.spacework.API

import com.android.spacework.model.Cart
import com.android.spacework.model.Order
import com.android.spacework.model.Orders
import com.android.spacework.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface API {

    //TARGET
    @POST("/add-product")
    @FormUrlEncoded
    fun addProduct(
        @Field("productId") productId: String,
        @Field("productName") productName: String,
        @Field("productDescription") productDescription: String,
        @Field("productPrice") productPrice: Double,
        @Field("productIsAvailable") productIsAvailable: Boolean,
        @Field("productImage") productImage: String,
        @Field("productCategory") productCategory : String
    ): Call<String>

    @POST("/add-user")
    @FormUrlEncoded
    fun addUser(
        @Field("userPhoneNumber") userPhoneNumber: String,
        @Field("userName") userName: String,
        @Field("userAddress") userAddress: String,
        @Field("userToken") userToken: String
    ): Call<String>

    @GET("/get-product-by-category")
    fun getProductByCategory(
        @Query("productCategory") productCategory: String
    ): Call<Array<Product>>

    @PATCH("/update-product")
    @FormUrlEncoded
    fun updateProduct(
        @Field("productName") productName: String,
        @Field("productDescription") productDescription: String,
        @Field("productPrice") productPrice: Double,
        @Field("productIsAvailable") productIsAvailable: Boolean,
        @Field("productImage") productImage: String
    ): Call<String>

    @PATCH("/update-user")
    @FormUrlEncoded
    fun updateUser(
        @Field("userPhoneNumber") userPhoneNumber: String,
        @Field("userAddress") userAddress: String,
        @Field("userName") userName: String
    ): Call<String>

    @POST("/delete-product")
    @FormUrlEncoded
    fun deleteProduct(
        @Field("productId") productId: String
    ): Call<String>

    @GET("/get-all-products")
    fun getAllProducts(): Call<Array<Product>>

    @GET("/get-product-by-id")
    fun getProductById(
        @Query("productId") productId: String
    ): Call<Product>

    @POST("/post-order")
    @FormUrlEncoded
    fun postOrder(
        @Field("userPhoneNumber") userPhoneNumber: String,
        @Field("userName") userName: String,
        @Field("userAddress") userAddress: String,
        @Field("orderHashmap") orderHashmap: String,
        @Field("orderTotal") orderTotal: Double
    ) : Call<String>

    @GET("/get-order-by-userid")
    fun getOrdersByID(
        @Query("userPhoneNumber") userPhoneNumber: String
    ) : Call<Array<Order>>


    @GET("/get-all-orders")
    fun getAllOrders() : Call<Array<Orders>>

    @PATCH("/order-by-id-haspaid")
    @FormUrlEncoded
    fun updatePayStatus(
        @Field("userPhoneNumber") userPhoneNumber: String,
        @Field("orderHashmap") orderHashmap: String,
        @Field("orderHasPaid") orderHasPaid: String
    ) : Call<String>


    @PATCH("/order-by-id-delivery")
    @FormUrlEncoded
    fun updateDeliveryStatus(
        @Field("userPhoneNumber") userPhoneNumber: String,
        @Field("orderHashmap") orderHashmap: String,
        @Field("orderStatus") orderStatus: String
    ) : Call<String>

    @PATCH("/cancel-order")
    @FormUrlEncoded
    fun cancelOrder(
        @Field("userPhoneNumber") userPhoneNumber: String,
        @Field("orderHashmap") orderHashmap: String
    ) : Call<String>

}