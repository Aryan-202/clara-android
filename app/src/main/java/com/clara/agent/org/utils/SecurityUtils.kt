package com.clara.agent.org.utils

import android.util.Base64
import java.security.SecureRandom

/**
 * Utility object providing security-related helpers.
 */
object SecurityUtils {

    private const val DEFAULT_NONCE_BYTE_LENGTH = 32

    /**
     * Generates a cryptographically secure random nonce encoded as a URL-safe Base64 string
     * without padding.
     *
     * @param byteLength number of random bytes to generate (default is 32)
     * @return Base64-encoded nonce string
     */
    fun generateSecureRandomNonce(byteLength: Int = DEFAULT_NONCE_BYTE_LENGTH): String {
        val randomBytes = ByteArray(byteLength)
        SecureRandom().nextBytes(randomBytes)
        return Base64.encodeToString(
            randomBytes,
            Base64.NO_WRAP or Base64.URL_SAFE or Base64.NO_PADDING
        )
    }
}