package com.vishalpvijayan.thefreshly.data.remote.model.DtoModel

data class ReviewDto(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)

