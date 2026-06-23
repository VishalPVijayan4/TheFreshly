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
import com.vishalpvijayan.thefreshly.utils.showFreshToast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.vishalpvijayan.thefreshly.domain.model.SavedLocation
import com.vishalpvijayan.thefreshly.helper.LocationViewModel
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.SavedLocationAdapter
import com.vishalpvijayan.thefreshly.presentation.vm.CheckoutViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.SavedLocationViewModel
import com.vishalpvijayan.thefreshly.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapsBinding
    private val locationViewModel: LocationViewModel by viewModels()
    private val savedLocationViewModel: SavedLocationViewModel by viewModels()

    private val checkoutViewModel: CheckoutViewModel by activityViewModels()

    private var googleMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private var isInitialLocationSet = false

    private var selectedLatitude: Double = 0.0
    private var selectedLongitude: Double = 0.0
    private var selectedAddress: String = ""
    private var selectedSavedLocation: SavedLocation? = null

    private lateinit var savedLocationAdapter: SavedLocationAdapter
    private var isDropdownVisible = false

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                enableMyLocation()
                centerOnCurrentLocation()
                locationViewModel.startUpdates()
            }
            else -> {
                requireContext().showFreshToast("Location permission required")
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

        // Repository address updates are only used before the user picks a map/saved location.
        // Manual selections resolve their own address, so background location updates cannot overwrite them.
        viewLifecycleOwner.lifecycleScope.launch {
            locationViewModel.addressFlow.collect { address ->
                if (address != null && selectedLatitude == 0.0 && selectedLongitude == 0.0) {
                    selectedAddress = address
                    binding.txtSelectedAddress.text = address
                    Log.d("MapFragment", "Address: $address")
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
                            // Create SavedLocation object and set it
                            val location = SavedLocation(
                                id = state.id.toInt(),
                                address = selectedAddress,
                                latitude = selectedLatitude,
                                longitude = selectedLongitude,
                                timestamp = System.currentTimeMillis()
                            )
                            checkoutViewModel.setSelectedAddress(location)

                            requireContext().showFreshToast("Location saved successfully!")
                            savedLocationViewModel.resetSaveState()
                            findNavController().navigateSafely(R.id.action_mapFragment_to_payment)
                        }


                        is SavedLocationViewModel.SaveState.Error -> {
                            requireContext().showFreshToast("Error: ${state.message}")
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
                selectedSavedLocation?.let { savedLocation ->
                    checkoutViewModel.setSelectedAddress(savedLocation)
                    findNavController().navigateSafely(R.id.action_mapFragment_to_payment)
                    return@setOnClickListener
                }

                savedLocationViewModel.saveLocation(
                    address = selectedAddress,
                    latitude = selectedLatitude,
                    longitude = selectedLongitude
                )
            } else {
                requireContext().showFreshToast("Please select a location")
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
            selectedSavedLocation = location
            selectedLatitude = location.latitude
            selectedLongitude = location.longitude
            selectedAddress = location.address
            binding.txtSelectedAddress.text = location.address
            binding.btnConfirmLocation.text = "Use this Location"
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
                centerOnCurrentLocation()
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
        centerOnCurrentLocation()
    }

    private fun centerOnCurrentLocation() {
        viewLifecycleOwner.lifecycleScope.launch {
            locationViewModel.getCurrentLocationOnce()?.let { location ->
                if (!isInitialLocationSet) {
                    moveToLocation(location.latitude, location.longitude)
                    isInitialLocationSet = true
                }
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun moveToLocation(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, 16f)
        )
        placeMarkerAndGetAddress(latLng)
    }

    private fun placeMarkerAndGetAddress(latLng: LatLng) {
        selectedSavedLocation = null
        binding.btnConfirmLocation.text = "Confirm Location"
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



