package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.thefreshly.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Handler(Looper.getMainLooper()).postDelayed({
            val action = SplashFragmentDirections.actionSplashToLogin()
            findNavController().navigate(action)
        }, 2000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}