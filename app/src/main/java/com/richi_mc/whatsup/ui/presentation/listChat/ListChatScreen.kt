package com.richi_mc.whatsup.ui.presentation.listChat

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.richi_mc.whatsup.ui.model.Chat
import com.richi_mc.whatsup.ui.theme.WhatsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListChatScreen(onNavigateToChat: (Int) -> Unit) {
    val listChatViewModel : ListChatViewModel = hiltViewModel()

    var showCreateChat by remember { mutableStateOf(false) }
    var showEnterChat  by remember { mutableStateOf(false) }

    val uiState = listChatViewModel.uiState.collectAsState()


    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Mis Chats")
                }
            )
        },
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,

                    onClick = {
                        showCreateChat = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = null
                    )
                }
                Spacer(Modifier.height(16.dp))
                FloatingActionButton(
                    onClick = {
                        showEnterChat = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        }

    ){ innerPadding ->

        Box {
            when(uiState.value) {
                ListChatUiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LinearProgressIndicator()
                    }
                }
                is ListChatUiState.Success -> {
                    val chats = (uiState.value as ListChatUiState.Success).chats
                    ListChats(chats, innerPadding) { index ->
                        onNavigateToChat(index)
                    }
                    if(showCreateChat) {
                        AlertCreateChat(
                            onCreateChat = { name ->
                                listChatViewModel.createNewChatGroup(name)
                            }
                        ) {
                            showCreateChat = false
                        }
                    }
                    if( showEnterChat ) {
                        AlertEnterChat {
                            showEnterChat = false
                        }
                    }
                }
            }
        }

    }
}
@Composable
fun AlertEnterChat(onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth(0.8f)
                .background(MaterialTheme.colorScheme.surface),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ingrese el ID del chat",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(8.dp))
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {

                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Unirse")
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
@Composable
fun AlertCreateChat(onCreateChat: (String) -> Unit, onDismissRequest: () -> Unit) {
    var chatName by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth(0.8f)
                .background(MaterialTheme.colorScheme.surface),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ingrese el nombre del chat",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = chatName,
                onValueChange = { chatName = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(8.dp))
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    onCreateChat(chatName)
                    onDismissRequest()
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                    Text("Crear")
                }
            }
        }
    }
}
@Composable
fun ListChats(chats: List<Chat>, innerPadding : PaddingValues, onChatClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(chats.size) { index ->
            Spacer(Modifier.height(8.dp))
            ChatItem(chats[index]) {
                // On item clicked
                onChatClick(index)
            }
        }
    }
}
@Composable
fun ChatItem( chat : Chat, onChatClick: () -> Unit ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                onChatClick()
            },
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(chat.image),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )

            Spacer(Modifier.width(24.dp))

            Text(
                text = "#${chat.id} ${chat.name}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
@Composable
@Preview(showSystemUi = true)
fun ChatScreenPreview() {
    WhatsAppTheme {
        ListChatScreen {

        }
    }
}