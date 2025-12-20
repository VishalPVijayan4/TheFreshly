package com.vishalpvijayan.thefreshly.domain.repository.location


import com.vishalpvijayan.thefreshly.domain.model.SavedLocation
import kotlinx.coroutines.flow.Flow

interface SavedLocationRepository {
    suspend fun saveLocation(location: SavedLocation): Long
    fun getAllLocations(): Flow<List<SavedLocation>>
    fun getLastSavedLocation(): Flow<SavedLocation?>
    suspend fun getLocationById(locationId: Int): SavedLocation?
    suspend fun deleteLocation(location: SavedLocation)
    suspend fun deleteAllLocations()
}
