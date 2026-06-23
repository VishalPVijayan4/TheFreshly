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
import com.google.android.material.button.MaterialButton
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.data.local.entity.CartItemEntity

class CartAdapter(
    private val onIncrement: (CartItemEntity) -> Unit,
    private val onDecrement: (CartItemEntity) -> Unit,
    private val onRemove: (CartItemEntity) -> Unit,
    private val onClick: (CartItemEntity) -> Unit
) : ListAdapter<CartItemEntity, CartAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgProduct: ImageView = view.findViewById(R.id.imgCartProduct)
        private val tvTitle: TextView = view.findViewById(R.id.tvCartProductTitle)
        private val tvBrand: TextView = view.findViewById(R.id.tvCartProductBrand)
        private val tvPrice: TextView = view.findViewById(R.id.tvCartProductPrice)
        private val tvQuantity: TextView = view.findViewById(R.id.tvCartQuantity)
        private val btnIncrease: MaterialButton = view.findViewById(R.id.btnCartIncrease)
        private val btnDecrease: MaterialButton = view.findViewById(R.id.btnCartDecrease)
        private val btnRemove: MaterialButton = view.findViewById(R.id.btnCartRemove)

        fun bind(item: CartItemEntity) {
            val unitPrice = "$${"%.2f".format(item.price)}"
            val lineTotal = item.price * item.quantity

            tvTitle.text = item.title
            tvBrand.text = "${item.quantity} units • $unitPrice"
            tvPrice.text = "$${"%.2f".format(lineTotal)}"
            tvQuantity.text = "${item.quantity} item${if (item.quantity == 1) "" else "s"}"

            imgProduct.load(item.thumbnail) {
                crossfade(true)
                placeholder(R.drawable.image_icon)
                error(R.drawable.image_icon)
            }

            btnIncrease.setOnClickListener { onIncrement(item) }
            btnDecrease.setOnClickListener { onDecrement(item) }
            btnRemove.setOnClickListener { onRemove(item) }
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartItemEntity>() {
            override fun areItemsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
                return oldItem.productId == newItem.productId
            }

            override fun areContentsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
