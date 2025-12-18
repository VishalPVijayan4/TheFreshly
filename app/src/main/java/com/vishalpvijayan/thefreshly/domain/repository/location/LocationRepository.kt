package com.vishalpvijayan.thefreshly.domain.repository.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedClient: FusedLocationProviderClient,
    private val locationManager: LocationManager
) {

    private val _locationFlow = MutableStateFlow<Location?>(null)
    val locationFlow: StateFlow<Location?> = _locationFlow

    private val _addressFlow = MutableStateFlow<String?>(null)
    val addressFlow: StateFlow<String?> = _addressFlow

    private var callback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if (!hasLocationPermission()) {
            Log.e("LocationRepository", "Location permission not granted")
            return
        }

        if (!isLocationEnabled()) {
            Log.e("LocationRepository", "Location services are disabled")
            return
        }

        if (callback != null) {
            Log.d("LocationRepository", "Location updates already running")
            return
        }

        // Get last known location
        fusedClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                Log.d("LocationRepository", "Last known location: ${it.latitude}, ${it.longitude}")
                _locationFlow.value = it

                // Get address from location
                CoroutineScope(Dispatchers.IO).launch {
                    val address = getAddressFromLocation(it)
                    _addressFlow.value = address
                }
            }
        }

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000
        ).build()

        callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { loc ->
                    Log.d("LocationRepository", "New location: ${loc.latitude}, ${loc.longitude}")
                    _locationFlow.value = loc

                    // Get address from location
                    CoroutineScope(Dispatchers.IO).launch {
                        val address = getAddressFromLocation(loc)
                        _addressFlow.value = address
                        Log.d("LocationRepository", "Address: $address")
                    }
                }
            }
        }

        fusedClient.requestLocationUpdates(
            request,
            callback!!,
            Looper.getMainLooper()
        ).addOnSuccessListener {
            Log.d("LocationRepository", "Location updates started successfully")
        }.addOnFailureListener { e ->
            Log.e("LocationRepository", "Failed to start location updates", e)
            callback = null
        }
    }

    fun stopLocationUpdates() {
        callback?.let {
            fusedClient.removeLocationUpdates(it)
            Log.d("LocationRepository", "Location updates stopped")
        }
        callback = null
    }

    private fun hasLocationPermission(): Boolean {
        return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private suspend fun getAddressFromLocation(location: Location): String? = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // For Android 13+
                var address: String? = null
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                ) { addresses ->
                    if (addresses.isNotEmpty()) {
                        address = addresses[0].getAddressLine(0)
                    }
                }
                delay(500) // Wait for callback
                return@withContext address
            } else {
                // For older versions
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                if (!addresses.isNullOrEmpty()) {
                    return@withContext addresses[0].getAddressLine(0)
                }
            }
        } catch (e: Exception) {
            Log.e("LocationRepository", "Geocoding failed", e)
        }
        return@withContext null
    }
}
