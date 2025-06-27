package com.vishalpvijayan.thefreshly

import com.google.gson.annotations.SerializedName


data class Meta (

  @SerializedName("createdAt" ) var createdAt : String? = null,
  @SerializedName("updatedAt" ) var updatedAt : String? = null,
  @SerializedName("barcode"   ) var barcode   : String? = null,
  @SerializedName("qrCode"    ) var qrCode    : String? = null

)