package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.lifecycle.ViewModel
import com.vishalpvijayan.thefreshly.domain.model.SavedLocation
import com.vishalpvijayan.thefreshly.domain.repository.location.SavedLocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val savedLocationRepository: SavedLocationRepository
) : ViewModel() {

    private val _selectedAddress = MutableStateFlow<SavedLocation?>(null)
    val selectedAddress: StateFlow<SavedLocation?> = _selectedAddress.asStateFlow()

    private val _deliveryInstructions = MutableStateFlow("")
    val deliveryInstructions: StateFlow<String> = _deliveryInstructions.asStateFlow()

    fun setSelectedAddress(address: SavedLocation) {
        _selectedAddress.value = address
    }

    fun setDeliveryInstructions(instructions: String) {
        _deliveryInstructions.value = instructions
    }

    // Delivery charges constant
    val deliveryCharges = 40.0
}
