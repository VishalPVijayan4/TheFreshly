package com.vishalpvijayan.thefreshly.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.vishalpvijayan.thefreshly.Products
import com.vishalpvijayan.thefreshly.R


class AllProductsAdapter(
    private val onClick: (Products) -> Unit
) : PagingDataAdapter<Products, AllProductsAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.txtProductName)
        private val productTitle: TextView = view.findViewById(R.id.product_title)
        private val productActualprice: TextView = view.findViewById(R.id.productActualprice)
        private val productDiscountedpercentage: TextView =
            view.findViewById(R.id.productDiscountedpercentage)
        private val productDiscountedprice: TextView =
            view.findViewById(R.id.productDiscountedprice)
        private val shipment: TextView = view.findViewById(R.id.shipment)
        private val icon: ImageView = view.findViewById(R.id.category_image)
        fun bind(item: Products, position: Int) {
            title.text = "\n"+item.title+"\n"
            productTitle.text = item.brand
            productActualprice.text = "Price \n$"+item.price.toString()
            productDiscountedprice.text = "Your Price \n$"+item.price.toString()
            productDiscountedpercentage.text = "Discount \n"+item.discountPercentage.toString()+"%"
            shipment.text = "Whats this product \n"+item.description
            itemView.setOnClickListener { onClick(item) }

            icon.load(item.thumbnail) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.ic_search)
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
