package com.vishalpvijayan.thefreshly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vishalpvijayan.thefreshly.data.local.dao.CartDao
import com.vishalpvijayan.thefreshly.data.local.dao.SavedLocationDao
import com.vishalpvijayan.thefreshly.data.local.entity.CartItemEntity
import com.vishalpvijayan.thefreshly.data.local.entity.SavedLocationEntity

@Database(
    entities = [SavedLocationEntity::class, CartItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedLocationDao(): SavedLocationDao
    abstract fun cartDao(): CartDao
}


