package com.vishalpvijayan.thefreshly

import com.google.gson.annotations.SerializedName


data class GetAllProducts (

  @SerializedName("products" ) var products : ArrayList<Products> = arrayListOf(),
  @SerializedName("total"    ) var total    : Int?                = null,
  @SerializedName("skip"     ) var skip     : Int?                = null,
  @SerializedName("limit"    ) var limit    : Int?                = null

)