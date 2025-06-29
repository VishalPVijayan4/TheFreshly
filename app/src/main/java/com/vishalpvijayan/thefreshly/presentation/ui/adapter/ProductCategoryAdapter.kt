package com.vishalpvijayan.thefreshly.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.data.remote.model.productCategory.ProductCategory
import com.vishalpvijayan.thefreshly.utils.CategoryListItem

/*class ProductCategoryAdapter(
    private val onClick: (ProductCategory) -> Unit
) : PagingDataAdapter<CategoryListItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        const val TYPE_CATEGORY = 0
        const val TYPE_BANNER = 1

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryListItem>() {
            override fun areItemsTheSame(oldItem: CategoryListItem, newItem: CategoryListItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: CategoryListItem, newItem: CategoryListItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.category_title)
        fun bind(item: ProductCategory) {
            title.text = item.name
            itemView.setOnClickListener { onClick(item) }
        }
    }

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.bannerImage)
        fun bind(imageRes: Int) {
            imageView.setImageResource(imageRes)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (peek(position)) {
            is CategoryListItem.CategoryItem -> TYPE_CATEGORY
            is CategoryListItem.StaticBanner -> TYPE_BANNER
            else -> TYPE_CATEGORY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BANNER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_static_banner, parent, false)
                BannerViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_raw_category, parent, false)
                CategoryViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is CategoryListItem.CategoryItem -> (holder as CategoryViewHolder).bind(item.category)
            is CategoryListItem.StaticBanner -> (holder as BannerViewHolder).bind(item.imageRes)
            null -> {}
        }
    }
}*/



class ProductCategoryAdapter(
    private val onClick: (ProductCategory) -> Unit
) : PagingDataAdapter<ProductCategory, ProductCategoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.category_title)
        private val icon: ImageView = view.findViewById(R.id.category_image)
        fun bind(item: ProductCategory, position: Int) {
            title.text = item.name
            icon.setImageResource(getImageForCategory(position,item))  // OR getImageForCategory(position, item)
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_raw_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it,position) }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductCategory>() {
            override fun areItemsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
                return oldItem.slug == newItem.slug
            }

            override fun areContentsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
                return oldItem == newItem
            }
        }
    }

    private fun getImageForCategory(position: Int, item: ProductCategory): Int {
        return when (item.slug) {
            "beauty" -> R.drawable.ic_category
            "womens-shoes" -> R.drawable.ic_category
            "womens-watches" -> R.drawable.ic_category
            else -> R.drawable.ic_launcher_foreground
        }
    }
}
