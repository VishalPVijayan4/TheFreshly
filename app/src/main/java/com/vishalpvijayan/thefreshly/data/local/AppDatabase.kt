package com.vishalpvijayan.thefreshly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vishalpvijayan.thefreshly.data.local.dao.CartDao
import com.vishalpvijayan.thefreshly.data.local.dao.SavedLocationDao
import com.vishalpvijayan.thefreshly.data.local.dao.QuickOrderDao
import com.vishalpvijayan.thefreshly.data.local.dao.SupportMessageDao
import com.vishalpvijayan.thefreshly.data.local.entity.CartItemEntity
import com.vishalpvijayan.thefreshly.data.local.entity.SavedLocationEntity
import com.vishalpvijayan.thefreshly.data.local.entity.QuickOrderEntity
import com.vishalpvijayan.thefreshly.data.local.entity.SupportMessageEntity

@Database(
    entities = [SavedLocationEntity::class, CartItemEntity::class, QuickOrderEntity::class, SupportMessageEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedLocationDao(): SavedLocationDao
    abstract fun cartDao(): CartDao
    abstract fun quickOrderDao(): QuickOrderDao

    abstract fun supportMessageDao(): SupportMessageDao
}


