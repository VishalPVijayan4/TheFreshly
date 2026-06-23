package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class ToolbarViewModel @Inject constructor() : ViewModel() {

    private val _toolbarTitle = MutableStateFlow("Default Title")
    val toolbarTitle: StateFlow<String> = _toolbarTitle

    private val _toolbarSubTitle = MutableStateFlow("Default Sub Title")
    val toolbarSubTitle: StateFlow<String> = _toolbarSubTitle

    fun setToolbarTitle(title: String, subTitle: String) {
        _toolbarTitle.value = title
        _toolbarSubTitle.value = subTitle
    }
}
