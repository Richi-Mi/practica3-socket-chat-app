package com.richi_mc.whatsup.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.richi_mc.whatsup.ui.presentation.chat.ChatScreen
import com.richi_mc.whatsup.ui.presentation.listChat.ListChatScreen
import com.richi_mc.whatsup.ui.presentation.login.LoginScreen

@Composable
fun WhatsUpNavigation() {

    val navController = rememberNavController()

    NavHost (
        navController = navController,
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreen {
                navController.navigate(ListChatRoute)
            }
        }
        composable<ListChatRoute> {
            ListChatScreen { index ->
                navController.navigate(ChatRoute(index))
            }
        }

        composable<ChatRoute> { backStackEntry ->
            val chat = backStackEntry.toRoute<ChatRoute>()
            ChatScreen(chat.chatId)
        }
    }
}