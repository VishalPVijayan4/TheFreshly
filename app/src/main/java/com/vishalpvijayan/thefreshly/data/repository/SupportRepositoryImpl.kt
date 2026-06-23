package com.vishalpvijayan.thefreshly.data.repository

import com.vishalpvijayan.thefreshly.data.local.dao.SupportMessageDao
import com.vishalpvijayan.thefreshly.data.local.entity.SupportMessageEntity
import com.vishalpvijayan.thefreshly.domain.model.SupportMessage
import com.vishalpvijayan.thefreshly.domain.repository.support.SupportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SupportRepositoryImpl @Inject constructor(
    private val dao: SupportMessageDao
) : SupportRepository {

    override fun getAllMessages(): Flow<List<SupportMessage>> {
        return dao.getAllMessages().map { entities ->
            entities.map { entity ->
                SupportMessage(
                    id = entity.id,
                    message = entity.message,
                    isUserMessage = entity.isUserMessage,
                    timestamp = entity.timestamp
                )
            }
        }
    }

    override suspend fun sendMessage(message: String) {
        dao.insertMessage(
            SupportMessageEntity(
                message = message,
                isUserMessage = true
            )
        )

        // Auto-reply from support
        kotlinx.coroutines.delay(1000) // Simulate delay
        dao.insertMessage(
            SupportMessageEntity(
                message = "Thank you for reaching out! Our team will get back to you soon.",
                isUserMessage = false
            )
        )
    }

    override suspend fun initializePredefinedMessages() {
        if (dao.getMessageCount() > 0) return

        val predefinedMessages = listOf(
            SupportMessageEntity(
                message = "Welcome to Freshly Support! How can we help you today?",
                isUserMessage = false,
                timestamp = System.currentTimeMillis() - 86400000 // 1 day ago
            ),
            SupportMessageEntity(
                message = "Hi, I have a question about delivery times.",
                isUserMessage = true,
                timestamp = System.currentTimeMillis() - 86300000
            ),
            SupportMessageEntity(
                message = "Our standard delivery time is within 10 minutes in your area. Is there anything specific you'd like to know?",
                isUserMessage = false,
                timestamp = System.currentTimeMillis() - 86200000
            )
        )
        dao.insertPredefinedMessages(predefinedMessages)
    }

    override suspend fun clearAllMessages() {
        dao.clearAllMessages()
    }
}
