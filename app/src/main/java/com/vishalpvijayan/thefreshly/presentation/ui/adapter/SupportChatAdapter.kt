package com.vishalpvijayan.thefreshly.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vishalpvijayan.thefreshly.databinding.ItemChatMessageSupportBinding
import com.vishalpvijayan.thefreshly.databinding.ItemChatMessageUserBinding

import com.vishalpvijayan.thefreshly.domain.model.SupportMessage
import java.text.SimpleDateFormat
import java.util.*

class SupportChatAdapter : ListAdapter<SupportMessage, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_SUPPORT = 2
        private fun formatTime(timestamp: Long): String {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isUserMessage) VIEW_TYPE_USER else VIEW_TYPE_SUPPORT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            UserMessageViewHolder(
                ItemChatMessageUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            SupportMessageViewHolder(
                ItemChatMessageSupportBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is UserMessageViewHolder -> holder.bind(message)
            is SupportMessageViewHolder -> holder.bind(message)
        }
    }

    class UserMessageViewHolder(
        private val binding: ItemChatMessageUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: SupportMessage) {
            binding.tvMessage.text = message.message
            binding.tvTimestamp.text = formatTime(message.timestamp)
        }
    }

    class SupportMessageViewHolder(
        private val binding: ItemChatMessageSupportBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: SupportMessage) {
            binding.tvMessage.text = message.message
            binding.tvTimestamp.text = formatTime(message.timestamp)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SupportMessage>() {
        override fun areItemsTheSame(oldItem: SupportMessage, newItem: SupportMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SupportMessage, newItem: SupportMessage): Boolean {
            return oldItem == newItem
        }
    }

    /*companion object {
        private fun formatTime(timestamp: Long): String {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }*/
}
