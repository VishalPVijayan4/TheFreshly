package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentLoginBinding
import com.vishalpvijayan.thefreshly.databinding.FragmentNotificationBinding
import com.vishalpvijayan.thefreshly.presentation.model.NotificationItem
import com.vishalpvijayan.thefreshly.presentation.ui.adapter.NotificationAdapter
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private val toolbarViewModel: ToolbarViewModel by activityViewModels()

    private lateinit var adapter: NotificationAdapter
    private lateinit var binding: FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarViewModel.setToolbarTitle("Notification", "Stay updated to whats the latest")


        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())

        val notifications = listOf(
            NotificationItem(R.drawable.ic_dress, "Order Confirmed","30 min ago", "Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully."),
            NotificationItem(R.drawable.ic_sports, "Payment Pending","1 D", "Please complete your payment.Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully."),
            NotificationItem(R.drawable.ic_dress, "Out for Delivery","2 D", "Your package is on the way.Your order has been placed successfully.Your order has been placed successfully."),
            NotificationItem(R.drawable.ic_sunglasses, "New Offer","2 D", "Get 50% off on your next purchase!.Your order has been placed successfully."),


            NotificationItem(R.drawable.ic_dress, "Order Confirmed","30 min ago", "Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully."),
            NotificationItem(R.drawable.ic_sports, "Payment Pending","1 D", "Please complete your payment.Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully."),
            NotificationItem(R.drawable.ic_dress, "Out for Delivery","2 D", "Your package is on the way.Your order has been placed successfully.Your order has been placed successfully."),
            NotificationItem(R.drawable.ic_sunglasses, "New Offer","2 D", "Get 50% off on your next purchase!.Your order has been placed successfully."),
            NotificationItem(R.drawable.ic_dress, "Order Confirmed","30 min ago", "Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully."),
            NotificationItem(R.drawable.ic_sports, "Payment Pending","1 D", "Please complete your payment.Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully.Your order has been placed successfully."),
            NotificationItem(R.drawable.ic_dress, "Out for Delivery","2 D", "Your package is on the way.Your order has been placed successfully.Your order has been placed successfully."),
            NotificationItem(R.drawable.ic_sunglasses, "New Offer","2 D", "Get 50% off on your next purchase!.Your order has been placed successfully."),
        )

        adapter = NotificationAdapter(notifications)
        binding.rvNotification.adapter = adapter
    }
}
