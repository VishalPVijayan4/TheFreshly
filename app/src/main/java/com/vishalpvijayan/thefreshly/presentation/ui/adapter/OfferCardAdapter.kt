package com.vishalpvijayan.thefreshly.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vishalpvijayan.thefreshly.databinding.ItemOfferCardBinding

data class OfferCard(
    val title: String,
    val subtitle: String,
    val action: String? = null
)

class OfferCardAdapter(
    private val offers: List<OfferCard>,
    private val onActionClick: () -> Unit
) : RecyclerView.Adapter<OfferCardAdapter.OfferCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferCardViewHolder {
        val binding = ItemOfferCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferCardViewHolder, position: Int) {
        holder.bind(offers[position])
    }

    override fun getItemCount(): Int = offers.size

    inner class OfferCardViewHolder(
        private val binding: ItemOfferCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(offer: OfferCard) {
            binding.tvOfferTitle.text = offer.title
            binding.tvOfferSubtitle.text = offer.subtitle
            binding.tvOfferAction.text = offer.action.orEmpty()
            binding.tvOfferAction.visibility = if (offer.action.isNullOrBlank()) android.view.View.GONE else android.view.View.VISIBLE
            binding.tvOfferAction.setOnClickListener { onActionClick() }
            binding.root.setOnClickListener { onActionClick() }
        }
    }
}
