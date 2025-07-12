package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentCreateAccountBinding
import com.vishalpvijayan.thefreshly.presentation.vm.CreateAccountVM
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class CreateAccountFragment : Fragment() {
    private lateinit var binding:  FragmentCreateAccountBinding
    private val viewModel: CreateAccountVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        binding.btnCreateAccount.setOnClickListener {

//            viewModel.addUser(binding.inputUsername.text.toString(),binding.inputPassword.text.toString(),binding.inputEmail.text.toString(),binding.inputPhoneNumber.text.toString().toInt())

            findNavController().navigate(R.id.action_createccount_to_login)
        }

        return binding.root
    }

}