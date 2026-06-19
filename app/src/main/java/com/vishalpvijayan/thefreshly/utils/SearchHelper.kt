package com.vishalpvijayan.thefreshly.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchHelper(
    private val searchEditText: EditText,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val debounceTime: Long = 500L,
    private val onSearchQuery: (String) -> Unit
) {

    private var searchJob: Job? = null
    private val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(debounceTime)
                    val query = s.toString().trim()
                    onSearchQuery(query)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
    }

    init {
        searchEditText.addTextChangedListener(textWatcher)
    }

    fun clearSearch() {
        searchEditText.text.clear()
    }

    fun dispose() {
        searchJob?.cancel()
        searchEditText.removeTextChangedListener(textWatcher)
    }
}
