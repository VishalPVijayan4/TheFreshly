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
import com.vishalpvijayan.thefreshly.utils.navigateSafely
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                Resource.Loading -> {
                    binding.btnLogin.isEnabled = false
                    ProgressDialogUtil.show(requireContext())
                }
                is Resource.Success -> {
                    binding.btnLogin.isEnabled = true
                    ProgressDialogUtil.hide()
                    Toast.makeText(requireContext(), "Welcome ${state.data.username}", Toast.LENGTH_SHORT).show()
                    if (findNavController().currentDestination?.id == R.id.login) {
                        findNavController().navigateSafely(R.id.action_login_to_dashboard)
                    }
                }
                is Resource.Error -> {
                    binding.btnLogin.isEnabled = true
                    ProgressDialogUtil.hide()
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.inputEmail.text.toString().trim(),
                binding.inputPassword.text.toString()
            )
        }
        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigateSafely(R.id.action_login_to_createccount2)
        }
        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigateSafely(R.id.action_login_to_forgotpassword)
        }
    }

    override fun onDestroyView() {
        ProgressDialogUtil.hide()
        _binding = null
        super.onDestroyView()
    }
}
