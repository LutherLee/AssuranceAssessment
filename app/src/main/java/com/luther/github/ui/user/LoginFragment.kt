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
import com.luther.github.databinding.FragmentLoginBinding
import com.luther.github.util.disableUIInteraction
import com.luther.github.util.enableUIInteraction
import com.luther.github.util.navigateTo
import com.luther.github.util.onDestroyNullable
import com.luther.github.util.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var binding by onDestroyNullable<FragmentLoginBinding>()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            val username = binding.loginUsernameTextInputEditText.text.toString()
            val password = binding.loginPasswordTextInputEditText.text.toString()
            when (userViewModel.isLoginDetailsValid(username, password)) {
                true -> verifyingLoginDetail(username, password)
                false -> showShortToast(getString(R.string.default_login_details_error_message))
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.signupButton.setOnClickListener {
            navigateTo(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginPasswordTextInputEditText.doAfterTextChanged { editable ->
            when (editable?.length!! > 0) {
                true ->
                    binding.loginPasswordTextInputLayout.setPasswordVisibilityToggleEnabled(true)

                false ->
                    binding.loginPasswordTextInputLayout.setPasswordVisibilityToggleEnabled(false)
            }
        }
    }

    private fun verifyingLoginDetail(email: String, password: String) {
        onLoginUserUpdateUI()
        lifecycleScope.launch {
            when (userViewModel.verifyLoginDetail(email, password)) {
                true -> navigateTo(R.id.action_loginFragment_to_movieListFragment)
                false -> showShortToast(getString(R.string.default_login_error_message))
            }
        }
        onFinishLoginUserUpdateUI()
    }

    private fun onLoginUserUpdateUI() {
        disableUIInteraction()
        binding.progressBar.visibility = View.VISIBLE
        binding.loginButton.text = ""
    }

    private fun onFinishLoginUserUpdateUI() {
        enableUIInteraction()
        binding.progressBar.visibility = View.GONE
        binding.loginButton.text = getString(R.string.login_text)
    }
}