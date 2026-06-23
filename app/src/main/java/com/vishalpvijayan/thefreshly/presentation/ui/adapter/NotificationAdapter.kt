package com.vishalpvijayan.thefreshly.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.presentation.model.NotificationItem

class NotificationAdapter(private val notifications: List<NotificationItem>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.ivIcon)
        val title: TextView = itemView.findViewById(R.id.txtTitle) // Update id if not set
        val time: TextView = itemView.findViewById(R.id.txtTime) // Update id if not set
        val description: TextView = itemView.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification_raw, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = notifications[position]
        holder.icon.setImageResource(item.iconResId)
        holder.title.text = item.title
        holder.time.text = item.time
        holder.description.text = item.description
    }

    override fun getItemCount(): Int = notifications.size
}
