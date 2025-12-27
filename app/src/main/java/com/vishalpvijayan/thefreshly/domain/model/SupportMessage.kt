package com.vishalpvijayan.thefreshly.domain.model


data class SupportMessage(
    val id: Int = 0,
    val message: String,
    val isUserMessage: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
