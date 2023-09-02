package com.luther.github.ui.user

import androidx.lifecycle.ViewModel
import com.luther.github.data.model.User
import com.luther.github.data.repository.UserRepository
import com.luther.github.di.IoDispatcher
import com.luther.github.util.SHA1Hashing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @IoDispatcher private val IoDispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository
) : ViewModel() {
    fun isLoginDetailsValid(username: String, password: String): Boolean {
        val isUsernameValid = username.isNotEmpty()
        val isPasswordValid = password.length >= 6
        return isUsernameValid && isPasswordValid
    }

    suspend fun verifyLoginDetail(username: String, password: String): Boolean =
         loginWithHardcodedCredentials(username, password)

    /**
     * For sample only. TODO: Remove this when no needed
     */
    private suspend fun loginWithHardcodedCredentials(username: String, password: String): Boolean {
        val hardcodedUsername = "VVVBB,"
        val hardcodedPassword = "@bcd1234"
        val isLoginSuccessful =
            (hardcodedUsername.equals(username)) && (hardcodedPassword.equals(password))
        if (isLoginSuccessful) {
            return true
        }
        return loginWithDefaultProcedure(username, password)
    }

    private suspend fun loginWithDefaultProcedure(username: String, password: String): Boolean {
        val user = withContext(IoDispatcher) {
            userRepository.fetchUserByUsername(username)
        } ?: return false
        val hashedPasswordInDB = user.password
        return verifyPassword(password, hashedPasswordInDB)
    }

    fun isSignUpDetailsValid(username: String, password: String, confirmPassword: String): Boolean {
        val isUsernameValid = username.isNotEmpty()
        val isPasswordValid = password.length >= 6
        val isConfirmPasswordValid = password.equals(confirmPassword)
        return isUsernameValid && isPasswordValid && isConfirmPasswordValid
    }

    /**
     * Compare password with the hashed password in database
     */
    private fun verifyPassword(password: String, hashedPasswordInDB: String): Boolean {
        val password = SHA1Hashing.hashString(password)
        return password.equals(hashedPasswordInDB)
    }

    suspend fun registerUser(username: String, password: String): Boolean {
        val hashedPassword = SHA1Hashing.hashString(password)
        val user = User(username, hashedPassword)
        // If successful, value will be positive
        val isRegistrationSuccesful = userRepository.registerUser(user) > 0
        return isRegistrationSuccesful
    }
}