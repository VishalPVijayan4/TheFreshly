package com.vishalpvijayan.thefreshly.helper

import android.location.Location
import androidx.lifecycle.ViewModel
import com.vishalpvijayan.thefreshly.domain.repository.location.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
/*@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repo: LocationRepository
) : ViewModel() {

    val locationFlow = repo.locationFlow
    val addressFlow = repo.addressFlow

    fun startUpdates() = repo.startLocationUpdates()
    suspend fun getCurrentLocationOnce() = repo.getCurrentLocationOnce()
    fun stopUpdates() = repo.stopLocationUpdates()

    override fun onCleared() {
        super.onCleared()
        repo.stopLocationUpdates()
    }
}*/


@HiltViewModel
class LocationViewModel @Inject constructor(
    val repo: LocationRepository  // Change from private to public
) : ViewModel() {

    val locationFlow = repo.locationFlow
    val addressFlow = repo.addressFlow

    fun startUpdates() = repo.startLocationUpdates()
    suspend fun getCurrentLocationOnce() = repo.getCurrentLocationOnce()
    fun stopUpdates() = repo.stopLocationUpdates()

    override fun onCleared() {
        super.onCleared()
        repo.stopLocationUpdates()
    }
}












