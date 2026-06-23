package com.vishalpvijayan.thefreshly.data.mapper


import com.vishalpvijayan.thefreshly.data.local.entity.SavedLocationEntity
import com.vishalpvijayan.thefreshly.domain.model.SavedLocation

fun SavedLocationEntity.toDomain(): SavedLocation {
    return SavedLocation(
        id = id,
        address = address,
        latitude = latitude,
        longitude = longitude,
        timestamp = timestamp
    )
}

fun SavedLocation.toEntity(): SavedLocationEntity {
    return SavedLocationEntity(
        id = id,
        address = address,
        latitude = latitude,
        longitude = longitude,
        timestamp = timestamp
    )
}
