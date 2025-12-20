package com.vishalpvijayan.thefreshly.data.repository



import com.vishalpvijayan.thefreshly.data.local.dao.SavedLocationDao
import com.vishalpvijayan.thefreshly.data.mapper.toDomain
import com.vishalpvijayan.thefreshly.data.mapper.toEntity
import com.vishalpvijayan.thefreshly.domain.model.SavedLocation
import com.vishalpvijayan.thefreshly.domain.repository.location.SavedLocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedLocationRepositoryImpl @Inject constructor(
    private val dao: SavedLocationDao
) : SavedLocationRepository {

    override suspend fun saveLocation(location: SavedLocation): Long {
        return dao.insertLocation(location.toEntity())
    }

    override fun getAllLocations(): Flow<List<SavedLocation>> {
        return dao.getAllLocations().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getLastSavedLocation(): Flow<SavedLocation?> {
        return dao.getLastSavedLocation().map { it?.toDomain() }
    }

    override suspend fun getLocationById(locationId: Int): SavedLocation? {
        return dao.getLocationById(locationId)?.toDomain()
    }

    override suspend fun deleteLocation(location: SavedLocation) {
        dao.deleteLocation(location.toEntity())
    }

    override suspend fun deleteAllLocations() {
        dao.deleteAllLocations()
    }
}
