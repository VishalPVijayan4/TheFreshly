package com.vishalpvijayan.thefreshly.presentation.vm


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.thefreshly.domain.model.SavedLocation
import com.vishalpvijayan.thefreshly.domain.repository.location.SavedLocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedLocationViewModel @Inject constructor(
    private val repository: SavedLocationRepository
) : ViewModel() {

    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

    val allLocations = repository.getAllLocations()
    val lastSavedLocation = repository.getLastSavedLocation()

    fun saveLocation(address: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _saveState.value = SaveState.Saving
            try {
                val location = SavedLocation(
                    address = address,
                    latitude = latitude,
                    longitude = longitude
                )
                val id = repository.saveLocation(location)
                _saveState.value = SaveState.Success(id)
            } catch (e: Exception) {
                _saveState.value = SaveState.Error(e.message ?: "Failed to save location")
            }
        }
    }

    fun resetSaveState() {
        _saveState.value = SaveState.Idle
    }

    sealed class SaveState {
        object Idle : SaveState()
        object Saving : SaveState()
        data class Success(val id: Long) : SaveState()
        data class Error(val message: String) : SaveState()
    }
}
