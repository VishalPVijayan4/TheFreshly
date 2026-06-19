package com.vishalpvijayan.thefreshly.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory

class CategoryListAdapter(
    private val onClick: (ProductCategory) -> Unit
) : ListAdapter<ProductCategory, CategoryListAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.category_title)
        private val icon: ImageView = view.findViewById(R.id.category_image)
        private val description: TextView = view.findViewById(R.id.category_description)

        fun bind(item: ProductCategory) {
            val displayInfo = ProductCategoryAdapter.getDisplayInfoForCategory(item)
            title.text = item.name
            icon.setImageResource(displayInfo.imageRes)
            description.text = displayInfo.description
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_raw_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductCategory>() {
            override fun areItemsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
                return oldItem.slug == newItem.slug
            }

            override fun areContentsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
                return oldItem == newItem
            }
        }
    }
}
