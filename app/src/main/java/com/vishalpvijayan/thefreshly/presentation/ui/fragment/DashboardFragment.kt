package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentDashboardBinding
import com.vishalpvijayan.thefreshly.databinding.FragmentLoginBinding
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel

class DashboardFragment : Fragment() {
    private lateinit var binding:  FragmentDashboardBinding

    private val toolbarViewModel: ToolbarViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        toolbarViewModel.setToolbarTitle("Dashboard","Manage & Explore various categories")
        return binding.root
    }
}