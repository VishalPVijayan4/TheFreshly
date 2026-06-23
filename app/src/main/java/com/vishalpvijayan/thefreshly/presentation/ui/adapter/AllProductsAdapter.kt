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

class AllProductsAdapter(
    private val onClick: (Products) -> Unit,
    private val onAddToCart: (Products) -> Unit,
    private val onIncrement: (Products) -> Unit,
    private val onDecrement: (Products) -> Unit,
    private val getCartQuantity: (Int) -> Int?
) : PagingDataAdapter<Products, AllProductsAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.txtProductName)
        private val productTitle: TextView = view.findViewById(R.id.product_title)
        private val productActualprice: TextView = view.findViewById(R.id.productActualprice)
        private val productDiscountedpercentage: TextView = view.findViewById(R.id.productDiscountedpercentage)
        private val productDiscountedprice: TextView = view.findViewById(R.id.productDiscountedprice)
        private val shipment: TextView = view.findViewById(R.id.shipment)
        private val icon: ImageView = view.findViewById(R.id.category_image)

        // Cart controls (add these to your item_products.xml if not present)
        private val btnAddToCart: MaterialButton? = view.findViewById(R.id.btnAddToCart)
        private val quantityControls: View? = view.findViewById(R.id.quantityControls)
        private val btnIncrease: MaterialButton? = view.findViewById(R.id.btnIncrease)
        private val btnDecrease: MaterialButton? = view.findViewById(R.id.btnDecrease)
        private val tvQuantityCount: TextView? = view.findViewById(R.id.tvQuantityCount)

        fun bind(item: Products, position: Int) {
            val price = item.price ?: 0.0
            val discount = item.discountPercentage ?: 0.0
            val listPrice = if (discount > 0.0 && discount < 100.0) {
                price / (1 - (discount / 100.0))
            } else {
                price
            }
            val dimensions = item.dimensions
            val dimensionText = dimensions?.let {
                "${it.width} x ${it.height} x ${it.depth} cm"
            }.orEmpty()
            val tags = item.tags.joinToString(" • ").ifBlank { "No tags" }

            title.text = item.title.orEmpty()
            productTitle.text = listOfNotNull(
                item.brand?.takeIf { it.isNotBlank() },
                item.category?.takeIf { it.isNotBlank() },
                item.rating?.let { "$it★" }
            ).joinToString(" · ")
            productActualprice.text = "List $${String.format("%.2f", listPrice)} · SKU ${item.sku.orEmpty()} · ${item.weight ?: 0}g · MOQ ${item.minimumOrderQuantity ?: 0}"
            productDiscountedprice.text = "$${String.format("%.2f", price)}"
            productDiscountedpercentage.text = "-${String.format("%.1f", discount)}%"
            shipment.text = listOf(
                item.description.orEmpty(),
                "${item.stock ?: 0} in stock · ${item.availabilityStatus.orEmpty()}",
                "Ships: ${item.shippingInformation.orEmpty()}",
                "Warranty: ${item.warrantyInformation.orEmpty()}",
                "Returns: ${item.returnPolicy.orEmpty()}",
                "Size: $dimensionText · Tags: $tags"
            ).filter { it.isNotBlank() }.joinToString("\n")

            itemView.setOnClickListener { onClick(item) }

            icon.load(item.thumbnail) {
                crossfade(true)
                placeholder(R.drawable.image_icon)
                error(R.drawable.image_icon)
            }

            // Update cart UI based on quantity
            val quantity = getCartQuantity(item.id ?: 0)
            updateCartUI(quantity)

            // Cart button listeners
            btnAddToCart?.setOnClickListener { onAddToCart(item) }
            btnIncrease?.setOnClickListener { onIncrement(item) }
            btnDecrease?.setOnClickListener { onDecrement(item) }
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

    fun updateCartQuantities(previous: Map<Int, Int>, current: Map<Int, Int>) {
        val changedProductIds = (previous.keys + current.keys).filter { previous[it] != current[it] }.toSet()
        if (changedProductIds.isEmpty()) return

        snapshot().items.forEachIndexed { position, product ->
            if (product.id in changedProductIds) notifyItemChanged(position)
        }
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
