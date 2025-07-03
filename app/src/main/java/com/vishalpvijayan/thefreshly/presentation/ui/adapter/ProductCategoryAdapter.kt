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
import com.vishalpvijayan.thefreshly.presentation.model.CategoryDisplayInfo
import com.vishalpvijayan.thefreshly.utils.CategoryListItem

class ProductCategoryAdapter(
    private val onClick: (ProductCategory) -> Unit
) : PagingDataAdapter<ProductCategory, ProductCategoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.category_title)
        private val icon: ImageView = view.findViewById(R.id.category_image)
        private val description: TextView = view.findViewById(R.id.category_description)

        fun bind(item: ProductCategory, position: Int) {
            val displayInfo = getDisplayInfoForCategory(position, item)

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
        getItem(position)?.let { holder.bind(it, position) }
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

    private fun getDisplayInfoForCategory(position: Int, item: ProductCategory): CategoryDisplayInfo {
        return when (item.slug) {
            "beauty" -> CategoryDisplayInfo(R.drawable.ic_beauty, "Unlock your natural glow with premium beauty picks.")
            "fragrances" -> CategoryDisplayInfo(R.drawable.ic_fragnance, "Let every moment linger with irresistible scents.")
            "furniture" -> CategoryDisplayInfo(R.drawable.ic_furniture, "Craft a cozy corner with our elegant furniture.")
            "groceries" -> CategoryDisplayInfo(R.drawable.ic_grocery, "Fresh picks to fill your kitchen with goodness.")
            "home-decoration" -> CategoryDisplayInfo(R.drawable.ic_home_decoration, "Turn your house into a home with style.")
            "kitchen-accessories" -> CategoryDisplayInfo(R.drawable.ic_kitchen, "Cook up magic with the finest tools.")
            "laptops" -> CategoryDisplayInfo(R.drawable.ic_laptop, "Power through tasks with the latest laptops.")
            "mens-shirts" -> CategoryDisplayInfo(R.drawable.ic_shirt, "Stay sharp with stylish men's shirts.")
            "mens-shoes" -> CategoryDisplayInfo(R.drawable.ic_shoes_men, "Step up your game with premium footwear.")
            "mens-watches" -> CategoryDisplayInfo(R.drawable.ic_men_watch, "Timeless style for every man’s wrist.")
            "mobile-accessories" -> CategoryDisplayInfo(R.drawable.ic_mobile, "Accessorize your tech the smart way.")
            "motorcycle" -> CategoryDisplayInfo(R.drawable.ic_motor, "Ride bold, ride beyond limits.")
            "skin-care" -> CategoryDisplayInfo(R.drawable.ic_skin, "Radiance redefined with every application.")
            "smartphones" -> CategoryDisplayInfo(R.drawable.ic_mobile_acc, "Smart choices for smarter living.")
            "sports-accessories" -> CategoryDisplayInfo(R.drawable.ic_sports, "Fuel your fitness with the right gear.")
            "sunglasses" -> CategoryDisplayInfo(R.drawable.ic_sunglasses, "Shade your eyes in effortless style.")
            "tablets" -> CategoryDisplayInfo(R.drawable.ic_tablets, "Portability meets performance in our tablets.")
            "tops" -> CategoryDisplayInfo(R.drawable.ic_dress, "Trendy tops that speak your style.")
            "vehicle" -> CategoryDisplayInfo(R.drawable.ic_vehichle, "Vehicles that take you places—literally.")
            "womens-bags" -> CategoryDisplayInfo(R.drawable.ic_bag, "Carry confidence with every step.")
            "womens-dresses" -> CategoryDisplayInfo(R.drawable.ic_dress, "Graceful fashion for every mood.")
            "womens-jewellery" -> CategoryDisplayInfo(R.drawable.ic_jewellary, "Sparkle with timeless elegance.")
            "womens-shoes" -> CategoryDisplayInfo(R.drawable.ic_women_shoe, "Stride in style with curated shoes.")
            "womens-watches" -> CategoryDisplayInfo(R.drawable.ic_women_watch, "Elegant timepieces for every woman.")
            else -> CategoryDisplayInfo(R.drawable.image_icon, "Explore handpicked essentials.")
        }
    }
}


/*class ProductCategoryAdapter(
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
            "beauty" -> R.drawable.ic_beauty
            "fragrances" -> R.drawable.ic_fragnance
            "furniture" -> R.drawable.ic_furniture
            "groceries" -> R.drawable.ic_grocery
            "home-decoration" -> R.drawable.ic_home_decoration
            "kitchen-accessories"-> R.drawable.ic_kitchen
            "laptops"-> R.drawable.ic_laptop
            "mens-shirts"-> R.drawable.ic_shirt
            "mens-shoes"-> R.drawable.ic_shoes_men
            "mens-watches"-> R.drawable.ic_men_watch
            "mobile-accessories"-> R.drawable.ic_mobile
            "motorcycle"-> R.drawable.ic_motor
            "skin-care"-> R.drawable.ic_skin
            "smartphones"-> R.drawable.ic_mobile_acc
            "sports-accessories"-> R.drawable.ic_sports
            "sunglasses"-> R.drawable.ic_sunglasses
            "tablets"-> R.drawable.ic_tablets
            "tops"-> R.drawable.ic_dress
            "vehicle"-> R.drawable.ic_vehichle
            "womens-bags"-> R.drawable.ic_bag
            "womens-dresses"-> R.drawable.ic_dress
            "womens-jewellery"-> R.drawable.ic_jewellary
            "womens-shoes" -> R.drawable.ic_women_shoe
            "womens-watches" -> R.drawable.ic_women_watch
            else -> R.drawable.image_icon
        }
    }
}*/
