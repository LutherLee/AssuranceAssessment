package com.luther.github.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.luther.github.R
import com.luther.github.databinding.FragmentRegisterBinding
import com.luther.github.util.disableUIInteraction
import com.luther.github.util.enableUIInteraction
import com.luther.github.util.navigateTo
import com.luther.github.util.onDestroyNullable
import com.luther.github.util.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var binding by onDestroyNullable<FragmentRegisterBinding>()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.signupButton.setOnClickListener {
            val username = binding.signupUsernameTextInputEditText.text.toString()
            val password = binding.signupPasswordTextInputEditText.text.toString()
            val confirmPassword = binding.confirmPasswordTextInputEditText.text.toString()
            when (userViewModel.isSignUpDetailsValid(username, password, confirmPassword)) {
                true -> registerUser(username, password)
                false -> showShortToast(getString(R.string.default_sign_up_details_error_message))
            }
        }

        binding.signupPasswordTextInputEditText.doAfterTextChanged { editable ->
            when (editable?.length!! > 0) {
                true ->
                    binding.signupPasswordTextInputLayout.setPasswordVisibilityToggleEnabled(true)
                false ->
                    binding.signupPasswordTextInputLayout.setPasswordVisibilityToggleEnabled(false)
            }
        }

        binding.confirmPasswordTextInputEditText.doAfterTextChanged { editable ->
            when (editable?.length!! > 0) {
                true ->
                    binding.confirmPasswordTextInputLayout.setPasswordVisibilityToggleEnabled(true)
                false ->
                    binding.confirmPasswordTextInputLayout.setPasswordVisibilityToggleEnabled(false)
            }
        }
    }

    private fun registerUser(username: String, password: String) {
        onRegisteringUserUpdateUI()
        lifecycleScope.launch {
            when (userViewModel.registerUser(username, password)) {
                true -> {
                    showShortToast(getString(R.string.user_registered_message))
                    navigateTo(R.id.action_registerFragment_to_loginFragment)
                }
                false -> showShortToast(getString(R.string.default_sign_up_error_message))
            }
        }
        onFinishRegisteringUserUpdateUI()
    }

    private fun onRegisteringUserUpdateUI() {
        disableUIInteraction()
        binding.progressBar.visibility = View.VISIBLE
        binding.signupButton.text = ""
    }

    private fun onFinishRegisteringUserUpdateUI() {
        enableUIInteraction()
        binding.progressBar.visibility = View.GONE
        binding.signupButton.text = getString(R.string.sign_up_text)
    }
}