package com.richi_mc.whatsup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.richi_mc.whatsup.ui.navigation.WhatsUpNavigation
import com.richi_mc.whatsup.ui.theme.WhatsAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WhatsAppTheme {
                WhatsUpNavigation()
            }
        }
    }
}