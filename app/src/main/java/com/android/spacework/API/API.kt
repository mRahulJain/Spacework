package com.android.spacework.API

import com.android.spacework.model.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface API {

    @POST("/add_product")
    @FormUrlEncoded
    fun addProduct(
        @Field("productId") productId: String,
        @Field("productName") productName: String,
        @Field("productDescription") productDescription: String,
        @Field("productPrice") productPrice: Double,
        @Field("productIsAvailable") productIsAvailable: Boolean,
        @Field("productImage") productImage: String
    ): Call<String>

}