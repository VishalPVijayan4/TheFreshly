package com.vishalpvijayan.thefreshly.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "support_messages")
data class SupportMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val message: String,
    val isUserMessage: Boolean, // true for user, false for support
    val timestamp: Long = System.currentTimeMillis()
)
