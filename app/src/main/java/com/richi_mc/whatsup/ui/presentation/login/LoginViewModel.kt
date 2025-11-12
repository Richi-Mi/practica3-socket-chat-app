package com.richi_mc.whatsup.ui.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.richi_mc.whatsup.network.ChatModule.Companion.NETWORK_DATA_SOURCE
import com.richi_mc.whatsup.network.NetworkDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Named(NETWORK_DATA_SOURCE) private val networkDataSource: NetworkDataSource
) : ViewModel() {

    fun onSignIn(
        username : String,
        ipAddress : String,
        onSuccess : (String) -> Unit
    ) {
        networkDataSource.setHost(ipAddress)
        viewModelScope.launch(Dispatchers.IO) {
            val message = networkDataSource.sendUsername(username)
            withContext(Dispatchers.Main) {
                onSuccess(message)
            }
        }
    }
}