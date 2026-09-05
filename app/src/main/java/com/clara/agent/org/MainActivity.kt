package com.clara.agent.org

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.clara.agent.org.ui.theme.ClaraTheme

/**
 * Main entry point of the application.
 *
 * Sets up edge‑to‑edge display, applies the app theme, and hosts the root composable
 * [ClaraApp] inside a full‑screen [Surface].
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClaraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ClaraApp()
                }
            }
        }
    }
}