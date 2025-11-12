package com.richi_mc.whatsup.ui.presentation.listChat

import com.richi_mc.whatsup.ui.model.Chat

sealed class ListChatUiState {
    data class Success(val chats : List<Chat>) : ListChatUiState()
    object Loading : ListChatUiState()
}