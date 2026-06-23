package com.vishalpvijayan.thefreshly.data.local.dao


import androidx.room.*
import com.vishalpvijayan.thefreshly.data.local.entity.SupportMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupportMessageDao {

    @Query("SELECT * FROM support_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<SupportMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: SupportMessageEntity)

    @Query("SELECT COUNT(*) FROM support_messages")
    suspend fun getMessageCount(): Int

    @Query("DELETE FROM support_messages")
    suspend fun clearAllMessages()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPredefinedMessages(messages: List<SupportMessageEntity>)
}
