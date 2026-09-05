package com.clara.agent.org.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clara.agent.org.ui.components.ChatHeader
import com.clara.agent.org.ui.components.MessageInput
import com.clara.agent.org.ui.components.TypingIndicator
import com.clara.agent.org.ui.theme.ClaraTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * Data model representing a chat message.
 *
 * @param text the message content.
 * @param isUser `true` if the message is from the user, `false` if from the AI.
 * @param timestamp the time the message was created (milliseconds since epoch).
 */
data class Message(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Main chat screen composable.
 *
 * Displays the conversation, handles message input, and simulates AI responses.
 */
@Composable
fun ChatScreen() {
    var messages by remember {
        mutableStateOf(
            listOf(
                Message(
                    text = "Hello! I'm Clara, your AI agent. How can I help you today?",
                    isUser = false
                )
            )
        )
    }
    var isTyping by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Auto-scroll to the latest message when the list changes.
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            ChatHeader(title = "Clara AI")
        },
        bottomBar = {
            Column {
                TypingIndicator(isTyping = isTyping)
                MessageInput(
                    onSendMessage = { text ->
                        // Add user message
                        messages = messages + Message(text = text, isUser = true)

                        // Simulate AI typing and response
                        scope.launch {
                            isTyping = true
                            delay(MOCK_RESPONSE_DELAY_MS.milliseconds)
                            isTyping = false
                            messages = messages + Message(
                                text = "I've received your message: \"$text\". I'm processing it now.",
                                isUser = false
                            )
                        }
                    },
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }
    }
}

/**
 * A single chat bubble that aligns and styles differently depending on the sender.
 *
 * @param message the message to display.
 */
@Composable
fun MessageBubble(message: Message) {
    val isUser = message.isUser
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor = if (isUser) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }
    val textColor = if (isUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSecondary
    }
    val shape = if (isUser) {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 2.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 2.dp,
            bottomEnd = 16.dp
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(shape)
                .background(bubbleColor)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview() {
    ClaraTheme {
        ChatScreen()
    }
}

private const val MOCK_RESPONSE_DELAY_MS = 1500L