package com.richi_mc.whatsup.ui.presentation.listChat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richi_mc.whatsup.network.ChatModule.Companion.NETWORK_DATA_SOURCE
import com.richi_mc.whatsup.network.NetworkDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListChatViewModel @Inject constructor(
    @Named(NETWORK_DATA_SOURCE) val networkDataSource: NetworkDataSource
) : ViewModel() {


    private val _uiState : MutableStateFlow<ListChatUiState> = MutableStateFlow(ListChatUiState.Loading)
    val uiState : StateFlow<ListChatUiState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val chats = networkDataSource.chats
            _uiState.value = ListChatUiState.Success(chats)
        }
    }
    fun createNewChatGroup(name: String) {
        _uiState.value = ListChatUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            networkDataSource.createGroup(name)

            val chats = networkDataSource.chats
            _uiState.value = ListChatUiState.Success(chats)
        }
    }

    override fun onCleared() {
        networkDataSource.closeConnection()
        super.onCleared()
    }
}