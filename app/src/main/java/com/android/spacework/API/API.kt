package com.android.spacework.API

import com.android.spacework.model.Cart
import com.android.spacework.model.Product
import retrofit2.Call
import retrofit2.http.*

interface API {

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

}