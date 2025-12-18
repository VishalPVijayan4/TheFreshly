package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.presentation.vm.LoginVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private val loginViewModel: LoginVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Always use viewLifecycleOwner for collecting in fragments
        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.isLoggedIn.collect { isLoggedIn ->
                delay(2000) // Delay in coroutine instead of Handler
                if (isAdded) { // To avoid crash if fragment is not attached
                    val action = if (isLoggedIn) {
                        SplashFragmentDirections.actionSplashToOnboarding()
                    } else {
                        SplashFragmentDirections.actionSplashToLogin()
                    }
                    findNavController().navigate(action)
                }
            }
        }
    }
}


/*
@AndroidEntryPoint
class SplashFragment : Fragment() {
    private val loginViewModel: LoginVM by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycleScope.launch {
            loginViewModel.isLoggedIn.collect {
                Handler(Looper.getMainLooper()).postDelayed({
                    var action = if (it) {
                        SplashFragmentDirections.actionSplashToOnboarding()
                    } else {
                        SplashFragmentDirections.actionSplashToLogin()
                    }
                    findNavController().navigate(action)
                }, 2000)
            }
        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}*/
