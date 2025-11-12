package com.richi_mc.whatsup.ui.model

import androidx.annotation.DrawableRes

data class Chat(
    val id : Int,
    val name : String,
    @DrawableRes
    val image : Int
) {
    override fun toString(): String {
        return "Chat(id=$id, name='$name')"
    }
}