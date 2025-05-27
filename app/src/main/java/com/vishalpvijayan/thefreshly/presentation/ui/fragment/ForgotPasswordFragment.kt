package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentForgotPasswordBinding
import com.vishalpvijayan.thefreshly.databinding.FragmentLoginBinding


class ForgotPasswordFragment : Fragment() {
    private lateinit var binding:  FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        binding.btnReset.setOnClickListener {
            findNavController().navigate(R.id.action_forgotpassword_to_login)
        }

        return binding.root
    }

}