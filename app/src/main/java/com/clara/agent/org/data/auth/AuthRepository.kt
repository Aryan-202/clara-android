package com.clara.agent.org.data.auth

import android.content.Context

interface AuthRepository {
    suspend fun signInWithGoogle(context: Context, webClientId: String): AuthResult<String>
    suspend fun signOut(context: Context): AuthResult<Unit>
}
