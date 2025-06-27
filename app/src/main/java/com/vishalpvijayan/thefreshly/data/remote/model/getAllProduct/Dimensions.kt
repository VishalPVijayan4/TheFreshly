package com.vishalpvijayan.thefreshly

import com.google.gson.annotations.SerializedName


data class Dimensions (

  @SerializedName("width"  ) var width  : Double? = null,
  @SerializedName("height" ) var height : Double? = null,
  @SerializedName("depth"  ) var depth  : Double? = null

)