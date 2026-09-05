package com.clara.agent.org.data.auth

import android.content.Context

/**
 * Repository interface for authentication operations.
 */
interface AuthRepository {

    /**
     * Initiates Google Sign-In flow and returns the result.
     *
     * @param context Android context used for the Credential Manager.
     * @param webClientId OAuth 2.0 Web Client ID for Google Sign-In.
     * @return [AuthResult] containing the ID token on success, or an error/state otherwise.
     */
    suspend fun signInWithGoogle(
        context: Context,
        webClientId: String
    ): AuthResult<String>

    /**
     * Signs out the current user by clearing any stored credentials.
     *
     * @param context Android context used for the Credential Manager.
     * @return [AuthResult] indicating success or failure.
     */
    suspend fun signOut(context: Context): AuthResult<Unit>
}