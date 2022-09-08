package com.udacity.project4.ui.fragments.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.udacity.project4.databinding.SignupFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignupFragment: Fragment() {

    private val signupViewModel by viewModel<SignupViewModel>()

    private lateinit var binding: SignupFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = SignupFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = this@SignupFragment
            viewModel = signupViewModel
        }
    }
}