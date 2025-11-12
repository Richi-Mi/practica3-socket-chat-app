package com.richi_mc.whatsup.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object ListChatRoute

@Serializable
data class ChatRoute(val chatId: Int)

@Serializable
object LoginRoute