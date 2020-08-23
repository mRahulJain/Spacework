package com.android.spacework.API

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

    @POST("/delete-product")
    @FormUrlEncoded
    fun deleteProduct(
        @Field("productId") productId: String
    ): Call<String>
}