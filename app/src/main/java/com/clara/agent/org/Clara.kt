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

@Preview(showBackground = true)
@Composable
fun PreviewClaraApp() {
    ClaraTheme {
        ClaraApp()
    }
}