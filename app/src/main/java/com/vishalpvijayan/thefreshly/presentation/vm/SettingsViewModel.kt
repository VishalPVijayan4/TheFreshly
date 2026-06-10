package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.thefreshly.data.local.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            dataStoreManager.clearUserSession()
            onComplete()
        }
    }
}
