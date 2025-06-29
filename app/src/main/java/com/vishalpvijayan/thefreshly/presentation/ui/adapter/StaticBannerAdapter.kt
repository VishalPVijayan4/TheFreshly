package com.vishalpvijayan.thefreshly.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.presentation.model.BannerItem

class StaticBannerAdapter(private val bannerList: List<BannerItem>) :
    RecyclerView.Adapter<StaticBannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val bannerImage: ImageView = view.findViewById(R.id.bannerImage)
        private val bannerTitle: TextView = view.findViewById(R.id.bannerTitle)
        fun bind(bannerItem: BannerItem) {
            bannerImage.setImageResource(bannerItem.imageRes)
            bannerTitle.text = bannerItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_static_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerList[position])
    }

    override fun getItemCount() = bannerList.size
}
