package com.richi_mc.whatsup.ui.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richi_mc.whatsup.network.ChatModel
import com.richi_mc.whatsup.network.ChatModule.Companion.NETWORK_DATA_SOURCE
import com.richi_mc.whatsup.network.NetworkDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChatViewModel @Inject constructor(
    @Named(NETWORK_DATA_SOURCE) val networkDataSource: NetworkDataSource
) : ViewModel() {

    private val _chatModel = MutableStateFlow<ChatModel?>(null)
    val chatModel : StateFlow<ChatModel?> = _chatModel

    var chatId : Int = 0

    val json = Json {
        ignoreUnknownKeys = true
    }


    fun init(chatId: Int) {
        this.chatId = chatId
        // TODO: Charge messages
        viewModelScope.launch(Dispatchers.IO) {
            val messages = networkDataSource.getChatInfo(chatId)
            val chatModel = json.decodeFromString<ChatModel>(messages)

            withContext(Dispatchers.Main) {
                _chatModel.value = chatModel
            }
        }
    }

    fun sendMessage(content : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val messages = networkDataSource.sendMessage(content, chatId)
            val chatModel : ChatModel = json.decodeFromString(messages)
            withContext(Dispatchers.Main) {
                _chatModel.value = chatModel
            }
        }
    }
}