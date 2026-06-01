package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentOrderSuccessBinding
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderSuccessFragment : Fragment() {

    private var _binding: FragmentOrderSuccessBinding? = null
    private val binding get() = _binding!!
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarViewModel.setToolbarTitle("Order confirmed", "Your fresh picks are on the way")
        binding.btnBackHome.setOnClickListener {
            findNavController().navigate(R.id.action_orderSuccessFragment_to_dashboard)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
