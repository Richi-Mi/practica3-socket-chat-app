package com.richi_mc.whatsup.ui.model

data class Message(
    val id : Int,
    val isMine : Boolean,
    val content : String,
)