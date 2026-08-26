package com.clara.agent.org

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.clara.agent.org.ui.theme.ClaraTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClaraApp() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agent Clara"
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Text(
                text = "Welcome Back!!"
            )

            Button(onClick = { /* Handle click */ }) {
                Text("Click Me")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClaraApp() {
    ClaraTheme {
        ClaraApp()
    }
}