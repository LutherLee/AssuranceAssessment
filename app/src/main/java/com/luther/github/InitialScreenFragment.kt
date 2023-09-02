package com.luther.github

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.luther.github.databinding.FragmentInitialScreenBinding
import com.luther.github.util.navigateTo
import com.luther.github.util.onDestroyNullable
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InitialScreenFragment : Fragment() {

    private var binding by onDestroyNullable<FragmentInitialScreenBinding>()
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInitialScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        // Note: You might want to check user login status
        // here to auto navigate to homepage in a real app
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            navigateTo(R.id.action_initialScreenFragment_to_loginFragment)
        }

        binding.signupButton.setOnClickListener {
            navigateTo(R.id.action_initialScreenFragment_to_registerFragment)
        }
    }
}