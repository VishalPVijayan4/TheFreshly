package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishalpvijayan.thefreshly.databinding.FragmentSupportChatBinding
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.SupportChatAdapter
import com.vishalpvijayan.thefreshly.presentation.vm.SupportChatViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SupportChatFragment : Fragment() {

    private var _binding: FragmentSupportChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SupportChatViewModel by viewModels()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()

    private lateinit var chatAdapter: SupportChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupportChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarViewModel.setToolbarTitle("Support Chat", "We're here to help")

        setupRecyclerView()
        observeMessages()
        setupMessageInput()
    }

    private fun setupRecyclerView() {
        chatAdapter = SupportChatAdapter()
        binding.rvMessages.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
        }
    }

    private fun observeMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.messages.collect { messages ->
                    chatAdapter.submitList(messages)
                    if (messages.isNotEmpty()) {
                        binding.rvMessages.smoothScrollToPosition(messages.size - 1)
                    }
                }
            }
        }
    }

    private fun setupMessageInput() {
        binding.btnSend.setOnClickListener {
            sendMessage()
        }

        binding.etMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else {
                false
            }
        }
    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString().trim()
        if (message.isNotEmpty()) {
            viewModel.sendMessage(message)
            binding.etMessage.text?.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
