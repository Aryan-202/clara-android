package com.clara.agent.org.data.auth

sealed interface AuthResult<out T> {
    object Idle : AuthResult<Nothing>
    object Loading : AuthResult<Nothing>
    data class Success<T>(val data: T) : AuthResult<T>
    data class Error(val message: String, val throwable: Throwable? = null) : AuthResult<Nothing>
}
