package com.vishalpvijayan.thefreshly.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.button.MaterialButton
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.R

class SingleCategoryProductsAdapter(
    private val onClick: (Products) -> Unit,
    private val onAddToCart: (Products) -> Unit,
    private val onIncrement: (Products) -> Unit,
    private val onDecrement: (Products) -> Unit,
    private val getCartQuantity: (Int) -> Int?
) : PagingDataAdapter<Products, SingleCategoryProductsAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.txtProductName)
        private val productTitle: TextView = view.findViewById(R.id.product_title)
        private val productActualprice: TextView = view.findViewById(R.id.productActualprice)
        private val productDiscountedpercentage: TextView = view.findViewById(R.id.productDiscountedpercentage)
        private val productDiscountedprice: TextView = view.findViewById(R.id.productDiscountedprice)
        private val shipment: TextView = view.findViewById(R.id.shipment)
        private val icon: ImageView = view.findViewById(R.id.category_image)

        private val btnAddToCart: MaterialButton? = view.findViewById(R.id.btnAddToCart)
        private val quantityControls: View? = view.findViewById(R.id.quantityControls)
        private val btnIncrease: MaterialButton? = view.findViewById(R.id.btnIncrease)
        private val btnDecrease: MaterialButton? = view.findViewById(R.id.btnDecrease)
        private val tvQuantityCount: TextView? = view.findViewById(R.id.tvQuantityCount)

        fun bind(item: Products, position: Int) {
            title.text = "\n${item.title}\n"
            productTitle.text = item.brand
            productActualprice.text = "Price \n$${item.price}"
            productDiscountedprice.text = "Your Price \n$${item.price}"
            productDiscountedpercentage.text = "Discount \n${item.discountPercentage}%"
            shipment.text = "What's this product \n${item.description}"

            itemView.setOnClickListener { onClick(item) }

            icon.load(item.thumbnail) {
                crossfade(true)
                placeholder(R.drawable.image_icon)
                error(R.drawable.image_icon)
            }

            val quantity = getCartQuantity(item.id ?: 0)
            updateCartUI(quantity)

            btnAddToCart?.setOnClickListener {
                it.isClickable = false
                onAddToCart(item)
                it.postDelayed({ it.isClickable = true }, 500)
            }
            btnIncrease?.setOnClickListener {
                it.isClickable = false
                onIncrement(item)
                it.postDelayed({ it.isClickable = true }, 300)
            }
            btnDecrease?.setOnClickListener {
                it.isClickable = false
                onDecrement(item)
                it.postDelayed({ it.isClickable = true }, 300)
            }
        }

        private fun updateCartUI(quantity: Int?) {
            if (quantity != null && quantity > 0) {
                btnAddToCart?.isVisible = false
                quantityControls?.isVisible = true
                tvQuantityCount?.text = quantity.toString()
            } else {
                btnAddToCart?.isVisible = true
                quantityControls?.isVisible = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_products, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
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
