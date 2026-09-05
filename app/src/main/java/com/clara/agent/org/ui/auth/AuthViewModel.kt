package com.clara.agent.org.ui.auth

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clara.agent.org.BuildConfig
import com.clara.agent.org.data.auth.AuthRepository
import com.clara.agent.org.data.auth.AuthRepositoryImpl
import com.clara.agent.org.data.auth.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling authentication-related UI state and actions.
 *
 * @param authRepository repository used for authentication operations. Defaults to
 * [AuthRepositoryImpl].
 */
class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthResult<String>>(AuthResult.Idle)
    val authState: StateFlow<AuthResult<String>> = _authState.asStateFlow()

    /**
     * Initiates Google Sign-In using the provided [context].
     */
    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _authState.value = AuthResult.Loading
            val webClientId = BuildConfig.WEB_CLIENT_ID
            val result = authRepository.signInWithGoogle(context, webClientId)
            _authState.value = result
        }
    }

    /**
     * Simulates email sign-in after validating the email address.
     *
     * For demonstration purposes, it returns a mock token. Replace with real authentication
     * logic when integrating a backend.
     *
     * @param email the email address entered by the user.
     */
    fun signInWithEmail(email: String) {
        viewModelScope.launch {
            if (!isValidEmail(email)) {
                _authState.value = AuthResult.Error(INVALID_EMAIL_MESSAGE)
                return@launch
            }
            _authState.value = AuthResult.Loading
            // Mock email login logic for demonstration
            _authState.value = AuthResult.Success("mock_token_for_$email")
        }
    }

    /**
     * Resets the authentication state to [AuthResult.Idle].
     */
    fun resetAuthState() {
        _authState.value = AuthResult.Idle
    }

    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        private const val INVALID_EMAIL_MESSAGE = "Please enter a valid email address."
    }
}