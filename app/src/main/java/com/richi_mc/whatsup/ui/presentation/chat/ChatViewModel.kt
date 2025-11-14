package com.richi_mc.whatsup.ui.presentation.chat

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richi_mc.whatsup.network.model.ChatModel
import com.richi_mc.whatsup.network.ChatModule.Companion.NETWORK_DATA_SOURCE
import com.richi_mc.whatsup.network.NetworkDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ChatViewModel @Inject constructor(
    @ApplicationContext val context : Context,
    @Named(NETWORK_DATA_SOURCE) val networkDataSource: NetworkDataSource
) : ViewModel() {

    private val _chatModel = MutableStateFlow<ChatModel?>(null)
    val chatModel : StateFlow<ChatModel?> = _chatModel

    var chatId : Int = 0

    val json = Json {
        ignoreUnknownKeys = true
    }

    lateinit var receiveJob : Job
    fun init(chatId: Int) {
        this.chatId = chatId

        receiveJob = viewModelScope.launch(Dispatchers.IO) {
            networkDataSource.getFlowMessage().collect { data ->
                Log.d("ChatViewModel", "Mensaje recibido: $data")

                val chatModel = json.decodeFromString<ChatModel>(data)
                withContext(Dispatchers.Main) {
                    _chatModel.value = chatModel
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ChatViewModel", "Mande la petición para obtener la información del chat")
            networkDataSource.getChatInfo(chatId)
        }
    }

    fun sendMessage(content : String) {
        viewModelScope.launch(Dispatchers.IO) {
            networkDataSource.sendMessage(content, chatId)
        }
    }

    fun destroyMessageCollection() {
        Log.d("ChatViewModel", "Elimine la instancia del Flow para liberar la escucha de chats")
        viewModelScope.launch(context = Dispatchers.IO){
            // networkDataSource.leaveChat(chatId)
        }
        receiveJob.cancel()
    }

}