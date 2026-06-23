package com.vishalpvijayan.thefreshly.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.R

class CuratedProductAdapter(
    private val onClick: (Products) -> Unit
) : ListAdapter<Products, CuratedProductAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val productImage: ImageView = view.findViewById(R.id.imgProduct)
        private val productTag: TextView = view.findViewById(R.id.tvProductTag)
        private val productName: TextView = view.findViewById(R.id.tvProductName)
        private val productPrice: TextView = view.findViewById(R.id.tvProductPrice)

        fun bind(item: Products) {
            productTag.text = item.category.orEmpty().ifBlank { "Curated" }
            productName.text = item.title.orEmpty()
            productPrice.text = "$${String.format("%.2f", item.price ?: 0.0)}"
            productImage.load(item.thumbnail) {
                crossfade(true)
                placeholder(R.drawable.image_icon)
                error(R.drawable.image_icon)
            }
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_curated_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Products>() {
            override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
                return oldItem == newItem
            }
        }
    }
}
