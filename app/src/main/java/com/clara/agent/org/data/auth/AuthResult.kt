package com.clara.agent.org.data.auth

/**
 * Represents the result of an authentication operation.
 *
 * @param T the type of data returned on success.
 */
sealed interface AuthResult<out T> {

    /**
     * Indicates no authentication operation is currently in progress.
     */
    object Idle : AuthResult<Nothing>

    /**
     * Indicates an authentication operation is in progress.
     */
    object Loading : AuthResult<Nothing>

    /**
     * Represents a successful authentication operation.
     *
     * @param data the result data.
     */
    data class Success<T>(val data: T) : AuthResult<T>

    /**
     * Represents a failed authentication operation.
     *
     * @param message a human-readable error message.
     * @param throwable an optional underlying exception.
     */
    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : AuthResult<Nothing>
}