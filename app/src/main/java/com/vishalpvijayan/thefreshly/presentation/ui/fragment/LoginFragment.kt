package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentLoginBinding
import com.vishalpvijayan.thefreshly.presentation.vm.LoginVM
import com.vishalpvijayan.thefreshly.utils.ProgressDialogUtil
import com.vishalpvijayan.thefreshly.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding:  FragmentLoginBinding
    private val viewModel: LoginVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.inputEmail.text.toString(), binding.inputPassword.text.toString())

            viewModel.loginState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is Resource.Loading -> ProgressDialogUtil.show(requireContext())
                    is Resource.Success -> {
                        ProgressDialogUtil.hide()
                        Toast.makeText(requireContext(), "Welcome " +
                                ""+state.data.username, Toast.LENGTH_SHORT).show()
                        viewModel.saveUserToken(state.data.username.toString(),state.data.accessToken.toString())

                        findNavController().navigate(R.id.action_login_to_dashboard)
                    }
                    is Resource.Error -> {
                        ProgressDialogUtil.hide()
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        binding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_createccount2)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgotpassword)
        }
        return binding.root
    }


}