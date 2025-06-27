package com.vishalpvijayan.thefreshly

import com.google.gson.annotations.SerializedName


data class Reviews (

  @SerializedName("rating"        ) var rating        : Int?    = null,
  @SerializedName("comment"       ) var comment       : String? = null,
  @SerializedName("date"          ) var date          : String? = null,
  @SerializedName("reviewerName"  ) var reviewerName  : String? = null,
  @SerializedName("reviewerEmail" ) var reviewerEmail : String? = null

)