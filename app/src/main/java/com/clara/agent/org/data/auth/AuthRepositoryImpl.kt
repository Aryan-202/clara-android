package com.clara.agent.org.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.clara.agent.org.utils.SecurityUtils
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

/**
 * Implementation of [AuthRepository] that handles Google Sign-In using Credential Manager.
 */
class AuthRepositoryImpl : AuthRepository {

    override suspend fun signInWithGoogle(
        context: Context,
        webClientId: String
    ): AuthResult<String> {
        if (webClientId.isBlank() || webClientId == PLACEHOLDER_WEB_CLIENT_ID) {
            return AuthResult.Error(
                message = "Client ID is not configured. Please set WEB_CLIENT_ID in local.properties"
            )
        }

        val credentialManager = CredentialManager.create(context)
        val nonce = SecurityUtils.generateSecureRandomNonce()

        // Attempt 1: Modern Sign-In button flow
        try {
            Log.d(TAG, "Initiating Google Sign-In via GetSignInWithGoogleOption...")
            val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(
                serverClientId = webClientId
            )
                .setNonce(nonce)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            return parseCredentialResult(result.credential)
        } catch (e: GetCredentialCancellationException) {
            Log.i(TAG, "User canceled Google Sign-In: ${e.message}")
            return AuthResult.Idle
        } catch (e: GetCredentialException) {
            Log.w(
                TAG,
                "GetSignInWithGoogleOption exception (${e.javaClass.simpleName}): " +
                        "${e.message}. Trying GetGoogleIdOption...",
                e
            )
        } catch (e: Exception) {
            Log.w(TAG, "Unexpected exception: ${e.message}. Trying GetGoogleIdOption...", e)
        }

        // Attempt 2: General Google ID option
        try {
            Log.d(TAG, "Initiating Google Sign-In via GetGoogleIdOption...")
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setNonce(nonce)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            return parseCredentialResult(result.credential)
        } catch (e: GetCredentialCancellationException) {
            Log.i(TAG, "User canceled Google Sign-In: ${e.message}")
            return AuthResult.Idle
        } catch (e: GetCredentialException) {
            Log.e(TAG, "GetGoogleIdOption failed (${e.javaClass.simpleName}): ${e.message}", e)
            return AuthResult.Error("Google Sign-In failed: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error during Google Sign-In", e)
            return AuthResult.Error("Google Sign-In failed: ${e.localizedMessage}")
        }
    }

    override suspend fun signOut(context: Context): AuthResult<Unit> {
        return try {
            val credentialManager = CredentialManager.create(context)
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing credential state", e)
            AuthResult.Error("Sign-out failed: ${e.localizedMessage}", e)
        }
    }

    private fun parseCredentialResult(credential: Any): AuthResult<String> {
        return when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    handleGoogleIdTokenCredential(credential)
                } else {
                    Log.e(TAG, "Unexpected custom credential type: ${credential.type}")
                    AuthResult.Error("Unexpected custom credential type: ${credential.type}")
                }
            }

            else -> {
                Log.e(TAG, "Unexpected credential type: ${credential.javaClass.name}")
                AuthResult.Error("Unexpected credential type: ${credential.javaClass.simpleName}")
            }
        }
    }

    private fun handleGoogleIdTokenCredential(credential: CustomCredential): AuthResult<String> {
        return try {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken
            val displayName = googleIdTokenCredential.displayName ?: googleIdTokenCredential.id
            Log.d(TAG, "Google Sign-In successful for: $displayName")
            AuthResult.Success(idToken)
        } catch (e: GoogleIdTokenParsingException) {
            Log.e(TAG, "Failed to parse Google ID token", e)
            AuthResult.Error("Failed to parse Google ID token: ${e.message}", e)
        }
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
        private const val PLACEHOLDER_WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID_HERE"
    }
}