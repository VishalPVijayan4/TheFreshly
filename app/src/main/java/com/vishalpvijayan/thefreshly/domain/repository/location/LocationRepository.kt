package com.vishalpvijayan.thefreshly.domain.repository.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class LocationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedClient: FusedLocationProviderClient,
    private val locationManager: LocationManager
) {
    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _locationFlow = MutableStateFlow<Location?>(null)
    val locationFlow: StateFlow<Location?> = _locationFlow
    private val _addressFlow = MutableStateFlow<String?>(null)
    val addressFlow: StateFlow<String?> = _addressFlow

    private var callback: LocationCallback? = null
    private var geocodeJob: Job? = null
    private var lastGeocodedLocation: Location? = null

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if (!hasLocationPermission() || !isLocationEnabled() || callback != null) return

        fusedClient.lastLocation.addOnSuccessListener { location ->
            location?.let(::handleLocation)
        }

        val request = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10_000L)
            .setMinUpdateIntervalMillis(5_000L)
            .setMinUpdateDistanceMeters(25f)
            .build()

        callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let(::handleLocation)
            }
        }

        fusedClient.requestLocationUpdates(request, callback!!, Looper.getMainLooper())
            .addOnFailureListener { error ->
                Log.e("LocationRepository", "Failed to start location updates", error)
                callback = null
            }
    }

    fun stopLocationUpdates() {
        callback?.let(fusedClient::removeLocationUpdates)
        callback = null
        geocodeJob?.cancel()
    }

    private fun handleLocation(location: Location) {
        _locationFlow.value = location
        val previous = lastGeocodedLocation
        if (previous != null && previous.distanceTo(location) < 25f) return
        lastGeocodedLocation = location

        // Only the newest location is geocoded. This prevents a queue of expensive geocoder calls
        // from building up while location callbacks are delivered on the main looper.
        geocodeJob?.cancel()
        geocodeJob = repositoryScope.launch {
            _addressFlow.value = getAddressFromLocation(location)
        }
    }

    private fun hasLocationPermission(): Boolean =
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun isLocationEnabled(): Boolean =
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocationOnce(): Location? {
        if (!hasLocationPermission() || !isLocationEnabled()) return null
        return suspendCancellableCoroutine { continuation ->
            fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { freshLocation ->
                    if (freshLocation != null && continuation.isActive) {
                        continuation.resume(freshLocation)
                    } else {
                        fusedClient.lastLocation
                            .addOnSuccessListener { lastLocation ->
                                if (continuation.isActive) continuation.resume(lastLocation)
                            }
                            .addOnFailureListener { error ->
                                Log.e("LocationRepository", "Failed to fetch last location", error)
                                if (continuation.isActive) continuation.resume(null)
                            }
                    }
                }
                .addOnFailureListener { error ->
                    Log.e("LocationRepository", "Failed to fetch current location", error)
                    if (continuation.isActive) continuation.resume(null)
                }
        }
    }

    suspend fun getAddressFromLocation(location: Location): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { continuation ->
                    geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                        if (continuation.isActive) {
                            continuation.resume(addresses.firstOrNull()?.getAddressLine(0))
                        }
                    }
                }
            } else {
                withContext(Dispatchers.IO) {
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        ?.firstOrNull()
                        ?.getAddressLine(0)
                }
            }
        } catch (error: Exception) {
            Log.e("LocationRepository", "Geocoding failed", error)
            null
        }
    }
}
