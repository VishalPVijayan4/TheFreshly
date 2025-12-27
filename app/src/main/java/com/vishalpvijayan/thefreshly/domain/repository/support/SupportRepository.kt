package com.vishalpvijayan.thefreshly.domain.repository.support

import com.vishalpvijayan.thefreshly.domain.model.SupportMessage
import kotlinx.coroutines.flow.Flow

interface SupportRepository {
    fun getAllMessages(): Flow<List<SupportMessage>>
    suspend fun sendMessage(message: String)
    suspend fun initializePredefinedMessages()
    suspend fun clearAllMessages()
}
