package com.vishalpvijayan.thefreshly.presentation.ui.fragment


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentMapsBinding
import com.vishalpvijayan.thefreshly.helper.LocationViewModel
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.SavedLocationAdapter
import com.vishalpvijayan.thefreshly.presentation.vm.SavedLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapsBinding
    private val locationViewModel: LocationViewModel by viewModels()
    private val savedLocationViewModel: SavedLocationViewModel by viewModels()

    private var googleMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private var isInitialLocationSet = false

    private var selectedLatitude: Double = 0.0
    private var selectedLongitude: Double = 0.0
    private var selectedAddress: String = ""

    private lateinit var savedLocationAdapter: SavedLocationAdapter
    private var isDropdownVisible = false

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                enableMyLocation()
                locationViewModel.startUpdates()
            }
            else -> {
                Toast.makeText(context, "Location permission required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Collect location updates
        viewLifecycleOwner.lifecycleScope.launch {
            locationViewModel.locationFlow.collect { location ->
                location?.let {
                    if (!isInitialLocationSet) {
                        moveToLocation(it.latitude, it.longitude)
                        isInitialLocationSet = true
                    }
                }
            }
        }

        // Collect address updates
        viewLifecycleOwner.lifecycleScope.launch {
            locationViewModel.addressFlow.collect { address ->
                address?.let {
                    selectedAddress = it
                    binding.txtSelectedAddress.text = it
                    Log.d("MapFragment", "Address: $it")
                }
            }
        }

        // Collect saved locations
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                savedLocationViewModel.allLocations.collect { locations ->
                    savedLocationAdapter.submitList(locations)

                    // Update visibility and text based on saved locations
                    if (locations.isEmpty()) {
                        binding.tvSavedLocations.text = "No saved locations"
                        binding.tvSavedLocations.isEnabled = false
                    } else {
                        binding.tvSavedLocations.text = "Select from saved locations (${locations.size})"
                        binding.tvSavedLocations.isEnabled = true
                    }
                }
            }
        }

        // Observe save state
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                savedLocationViewModel.saveState.collect { state ->
                    when (state) {
                        is SavedLocationViewModel.SaveState.Idle -> {
                            binding.btnConfirmLocation.isEnabled = true
                            binding.btnConfirmLocation.text = "Confirm Location"
                        }
                        is SavedLocationViewModel.SaveState.Saving -> {
                            binding.btnConfirmLocation.isEnabled = false
                            binding.btnConfirmLocation.text = "Saving..."
                        }
                        is SavedLocationViewModel.SaveState.Success -> {
                            Toast.makeText(
                                context,
                                "Location saved successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            savedLocationViewModel.resetSaveState()
                            findNavController().navigateUp()
                        }
                        is SavedLocationViewModel.SaveState.Error -> {
                            Toast.makeText(
                                context,
                                "Error: ${state.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            savedLocationViewModel.resetSaveState()
                        }
                    }
                }
            }
        }

        // Toggle saved locations dropdown
        binding.tvSavedLocations.setOnClickListener {
            toggleDropdown()
        }

        // Confirm button click
        binding.btnConfirmLocation.setOnClickListener {
            if (selectedAddress.isNotEmpty() && selectedLatitude != 0.0 && selectedLongitude != 0.0) {
                savedLocationViewModel.saveLocation(
                    address = selectedAddress,
                    latitude = selectedLatitude,
                    longitude = selectedLongitude
                )
            } else {
                Toast.makeText(context, "Please select a location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        savedLocationAdapter = SavedLocationAdapter { location ->
            // Hide dropdown
            toggleDropdown()

            // Move map to selected location
            moveToLocation(location.latitude, location.longitude)

            // Update selected data
            selectedLatitude = location.latitude
            selectedLongitude = location.longitude
            selectedAddress = location.address
            binding.txtSelectedAddress.text = location.address
        }

        binding.rvSavedLocations.apply {
            adapter = savedLocationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun toggleDropdown() {
        isDropdownVisible = !isDropdownVisible

        binding.rvSavedLocations.visibility = if (isDropdownVisible) View.VISIBLE else View.GONE

        val drawableRes = if (isDropdownVisible) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
        binding.tvSavedLocations.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0, 0, drawableRes, 0
        )
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap?.apply {
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isCompassEnabled = true

            setOnMapClickListener { latLng ->
                placeMarkerAndGetAddress(latLng)
            }

            setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDragStart(marker: Marker) {
                    binding.txtSelectedAddress.text = "Moving marker..."
                }

                override fun onMarkerDrag(marker: Marker) {}

                override fun onMarkerDragEnd(marker: Marker) {
                    placeMarkerAndGetAddress(marker.position)
                }
            })
        }

        checkAndRequestLocationPermission()
    }

    private fun checkAndRequestLocationPermission() {
        when {
            hasLocationPermission() -> {
                enableMyLocation()
                locationViewModel.startUpdates()
            }
            else -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        googleMap?.isMyLocationEnabled = true
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun moveToLocation(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, 15f)
        )
        placeMarkerAndGetAddress(latLng)
    }

    private fun placeMarkerAndGetAddress(latLng: LatLng) {
        currentMarker?.remove()

        currentMarker = googleMap?.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Selected Location")
                .draggable(true)
        )

        selectedLatitude = latLng.latitude
        selectedLongitude = latLng.longitude

        binding.txtSelectedAddress.text = "Getting address..."

        viewLifecycleOwner.lifecycleScope.launch {
            val location = Location("").apply {
                latitude = latLng.latitude
                longitude = latLng.longitude
            }

            val address = locationViewModel.repo.getAddressFromLocation(location)
            selectedAddress = address ?: "Address not found"
            binding.txtSelectedAddress.text = selectedAddress
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationViewModel.stopUpdates()
    }
}



