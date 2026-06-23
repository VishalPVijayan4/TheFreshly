package com.vishalpvijayan.thefreshly.domain.model


data class SavedLocation(
    val id: Int = 0,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)
