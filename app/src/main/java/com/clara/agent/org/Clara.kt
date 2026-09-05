package com.clara.agent.org

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.clara.agent.org.ui.screens.ChatScreen
import com.clara.agent.org.ui.screens.LoginScreen
import com.clara.agent.org.ui.theme.ClaraTheme

/**
 * Root composable that decides whether to show the login screen or the chat screen
 * based on the current authentication state.
 */
@Composable
fun ClaraApp() {
    var isLoggedIn by remember { mutableStateOf(false) }

    if (isLoggedIn) {
        ChatScreen()
    } else {
        LoginScreen(
            onLoginSuccess = { isLoggedIn = true }
        )
    }
}

/**
 * Preview for [ClaraApp] wrapped in the app theme.
 */
@Preview(showBackground = true)
@Composable
private fun PreviewClaraApp() {
    ClaraTheme {
        ClaraApp()
    }
}