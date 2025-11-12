package com.richi_mc.whatsup.ui.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.richi_mc.whatsup.R

@Composable
fun LoginScreen(
    onNavigateToList: () -> Unit
) {
    val loginViewModel : LoginViewModel = hiltViewModel()
    val context = LocalContext.current

    var username  by remember { mutableStateOf("") }
    var ipAddress by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Bienvenido a What's up",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = username,
            onValueChange = { username = it },
            label = {
                Text(text = "Nombre de usuario")
            }
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.8f),
            value = ipAddress,
            onValueChange = { ipAddress = it },
            label = {
                Text("Dirección IP")
            }
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                loginViewModel.onSignIn(username, ipAddress) { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    onNavigateToList()
                }
            }
        ) {
            Text("Conectarse")
        }

    }
}