package com.vishalpvijayan.thefreshly.presentation.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vishalpvijayan.thefreshly.databinding.ItemSavedLocationBinding
import com.vishalpvijayan.thefreshly.domain.model.SavedLocation

class SavedLocationAdapter(
    private val onLocationClick: (SavedLocation) -> Unit
) : ListAdapter<SavedLocation, SavedLocationAdapter.SavedLocationViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedLocationViewHolder {
        val binding = ItemSavedLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SavedLocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedLocationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SavedLocationViewHolder(
        private val binding: ItemSavedLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: SavedLocation) {
            binding.apply {
                tvAddress.text = location.address
                tvCoordinates.text = "${String.format("%.4f", location.latitude)}, ${String.format("%.4f", location.longitude)}"

                root.setOnClickListener {
                    onLocationClick(location)
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<SavedLocation>() {
        override fun areItemsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
            return oldItem == newItem
        }
    }
}
