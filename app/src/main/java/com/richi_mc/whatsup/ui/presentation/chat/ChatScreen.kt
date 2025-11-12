package com.richi_mc.whatsup.ui.presentation.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.richi_mc.whatsup.network.MessageModel
import com.richi_mc.whatsup.ui.model.Message
import com.richi_mc.whatsup.ui.theme.WhatsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(id : Int) {
    val chatViewModel : ChatViewModel = hiltViewModel()
    val chatModel = chatViewModel.chatModel.collectAsState()

    LaunchedEffect(Unit) {
        chatViewModel.init(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Grupo de Wasap")
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if(chatModel.value != null) {
                val messages = chatModel.value!!.messages
                LazyColumn(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    items(messages.size) { index ->
                        MessageItem(message = messages[index])
                    }
                }
                SenderBox(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                ) { content ->
                    chatViewModel.sendMessage(content)
                }
            }
            else {
                LinearProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
@Composable
fun SenderBox(modifier: Modifier = Modifier, onSendMessage: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.7f),
            value = text,
            onValueChange = { text = it }
        )
        Button(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = {
                onSendMessage(text)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = null
            )
        }
    }
}
@Composable
fun MessageItem(message: MessageModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = if (message.owner == "richi_mc") Alignment.Start else Alignment.End
    ) {
        Card {
            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun ChatScreenPreview() {
    WhatsAppTheme {
        ChatScreen(1)
    }
}