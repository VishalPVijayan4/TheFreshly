package com.vishalpvijayan.thefreshly.data.local.dao


import androidx.room.*
import com.vishalpvijayan.thefreshly.data.local.entity.SavedLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: SavedLocationEntity): Long

    @Query("SELECT * FROM saved_locations ORDER BY timestamp DESC")
    fun getAllLocations(): Flow<List<SavedLocationEntity>>

    @Query("SELECT * FROM saved_locations ORDER BY timestamp DESC LIMIT 1")
    fun getLastSavedLocation(): Flow<SavedLocationEntity?>

    @Query("SELECT * FROM saved_locations WHERE id = :locationId")
    suspend fun getLocationById(locationId: Int): SavedLocationEntity?

    @Delete
    suspend fun deleteLocation(location: SavedLocationEntity)

    @Query("DELETE FROM saved_locations")
    suspend fun deleteAllLocations()
}
