package com.vishalpvijayan.thefreshly.data.remote.model.productDetail

data class Review(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)
