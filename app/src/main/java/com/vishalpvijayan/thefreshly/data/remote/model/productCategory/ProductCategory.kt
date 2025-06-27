package com.vishalpvijayan.thefreshly.data.remote.model.productCategory


import com.google.gson.annotations.SerializedName


data class ProductCategory (

    @SerializedName("slug" ) var slug : String? = null,
    @SerializedName("name" ) var name : String? = null,
    @SerializedName("url"  ) var url  : String? = null

)
