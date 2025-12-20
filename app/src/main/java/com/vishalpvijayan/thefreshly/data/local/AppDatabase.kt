package com.vishalpvijayan.thefreshly.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vishalpvijayan.thefreshly.data.local.dao.SavedLocationDao
import com.vishalpvijayan.thefreshly.data.local.entity.SavedLocationEntity

@Database(
    entities = [SavedLocationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedLocationDao(): SavedLocationDao
}
