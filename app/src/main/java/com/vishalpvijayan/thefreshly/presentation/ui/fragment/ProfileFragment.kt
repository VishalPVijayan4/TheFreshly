package com.vishalpvijayan.thefreshly.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentProfileBinding
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.UserDetailViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val userDetailsVM: UserDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        toolbarViewModel.setToolbarTitle("My Profile", "Manage your profile details & keep updated")

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userDetailsVM.userId.collect { userId ->

                    userDetailsVM.loadUserDetail(1)

                    userDetailsVM.user.collect { user ->
                        Log.d("UserData", "Received user: $user")
                        binding.imageProfile.load(user?.image) {
                            crossfade(true)
                            placeholder(R.drawable.image_icon)
                            error(R.drawable.image_icon)
                        }
                        binding.textFullName.text = user?.username + " " + user?.lastName
                        binding.textAgeGender.text = user?.age.toString()+ " "+ user?.gender
                        binding.textEmail.text = user?.email
                        binding.textPhone.text = user?.phone
                        binding.textUniversity.text = user?.university
                        binding.textAddress.text = user?.address?.address.orEmpty() + " "+ user?.address?.city.orEmpty()+ " "+ user?.address?.state.orEmpty()+ " "+ user?.address?.postalCode.orEmpty()
                    }
                }
            }
        }

        return binding.root
    }
}