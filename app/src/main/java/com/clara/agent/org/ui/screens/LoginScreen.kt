package com.clara.agent.org.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.clara.agent.org.data.auth.AuthResult
import com.clara.agent.org.ui.auth.AuthViewModel
import com.clara.agent.org.ui.components.OrDivider
import com.clara.agent.org.ui.components.SocialLoginButton
import com.clara.agent.org.ui.theme.ClaraIcons
import com.clara.agent.org.ui.theme.ClaraTheme

/**
 * Login screen that offers Google Sign-In, phone (placeholder), and email entry.
 *
 * @param onLoginSuccess callback invoked when login succeeds.
 * @param viewModel the [AuthViewModel] that manages authentication state.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()
    val scrollState = rememberScrollState()

    // Handle authentication state changes.
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthResult.Success -> {
                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                viewModel.resetAuthState()
                onLoginSuccess()
            }

            is AuthResult.Error -> {
                errorMessage = state.message
            }

            else -> Unit
        }
    }

    // Show error dialog if there is an error message.
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = {
                errorMessage = null
                viewModel.resetAuthState()
            },
            title = {
                Text(text = "Sign-In Information")
            },
            text = {
                Text(text = errorMessage.orEmpty())
            },
            confirmButton = {
                Button(
                    onClick = {
                        errorMessage = null
                        viewModel.resetAuthState()
                    }
                ) {
                    Text(text = "OK")
                }
            }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Log in or sign up",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            SocialLoginButton(
                text = "Continue with Google",
                onClick = { viewModel.signInWithGoogle(context) },
                containerColor = Color.White,
                contentColor = Color.Black,
                enabled = authState !is AuthResult.Loading,
                isLoading = authState is AuthResult.Loading,
                icon = {
                    Icon(
                        imageVector = ClaraIcons.Google,
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                }
            )

            SocialLoginButton(
                text = "Continue with phone",
                onClick = {
                    Toast.makeText(context, "Phone Sign-In coming soon", Toast.LENGTH_SHORT).show()
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                enabled = authState !is AuthResult.Loading,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )

            OrDivider()

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                enabled = authState !is AuthResult.Loading
            )

            Button(
                onClick = { viewModel.signInWithEmail(email) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = authState !is AuthResult.Loading
            ) {
                if (authState is AuthResult.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Continue",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    ClaraTheme {
        LoginScreen()
    }
}