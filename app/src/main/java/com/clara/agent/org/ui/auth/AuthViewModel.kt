package com.clara.agent.org.ui.auth

import android.content.Context
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

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthResult<String>>(AuthResult.Idle)
    val authState: StateFlow<AuthResult<String>> = _authState.asStateFlow()

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _authState.value = AuthResult.Loading
            val webClientId = BuildConfig.WEB_CLIENT_ID
            val result = authRepository.signInWithGoogle(context, webClientId)
            _authState.value = result
        }
    }

    fun signInWithEmail(email: String) {
        viewModelScope.launch {
            if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _authState.value = AuthResult.Error("Please enter a valid email address.")
                return@launch
            }
            _authState.value = AuthResult.Loading
            // Mock email login logic for demonstration
            _authState.value = AuthResult.Success("mock_token_for_$email")
        }
    }

    fun resetAuthState() {
        _authState.value = AuthResult.Idle
    }
}
