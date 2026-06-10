package com.vishalpvijayan.thefreshly.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishalpvijayan.thefreshly.domain.model.SupportMessage
import com.vishalpvijayan.thefreshly.domain.repository.support.SupportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupportChatViewModel @Inject constructor(
    private val repository: SupportRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<SupportMessage>>(emptyList())
    val messages: StateFlow<List<SupportMessage>> = _messages.asStateFlow()

    init {
        loadMessages()
        initializePredefinedMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            repository.getAllMessages().collect { messages ->
                _messages.value = messages
            }
        }
    }

    private fun initializePredefinedMessages() {
        viewModelScope.launch {
            repository.initializePredefinedMessages()
        }
    }

    fun sendMessage(message: String) {
        if (message.isBlank()) return
        viewModelScope.launch {
            repository.sendMessage(message)
        }
    }
}
