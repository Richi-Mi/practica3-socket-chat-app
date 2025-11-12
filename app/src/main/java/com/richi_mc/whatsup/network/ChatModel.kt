package com.richi_mc.whatsup.network

import kotlinx.serialization.Serializable

@Serializable
class ChatModel(
    val name : String,
    val messages : List<MessageModel>,
    val users: List<UserModel>
)

@Serializable
class MessageModel(
    val content : String,
    val owner : String
)
@Serializable
class UserModel(
    val name : String
)