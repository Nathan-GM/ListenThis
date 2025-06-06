package dam.nathan.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dam.nathan.darkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    isDarkModeOn: Boolean,
    register: () -> Unit,
    login: (suspend (String, String, CoroutineScope) -> String)? = null,
    changeTheme: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var valid by remember { mutableStateOf(false) }
    var timeout by remember { mutableStateOf(false) }
    var enabled = username.isNotBlank() && password.isNotBlank()
    var waiting by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Bienvenido a ListenThis",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it},
                label = { Text("Usuario") },
                placeholder = { Text("Introduce tu usuario") },
                singleLine = true,
                enabled = waiting == false
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it},
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { showPassword = !showPassword }
                    ) {
                        val icon = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(icon, contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña")
                    }
                },
                enabled = waiting == false
                )

            Spacer(modifier = Modifier.height(16.dp))

            if (valid) {
                Text(text = "Usuario o contraseña incorrectos",
                    color = MaterialTheme.colorScheme.error)
            }

            if (timeout) {
                Text(text = "No se pudo conectar al servidor",
                    color = MaterialTheme.colorScheme.error)
            }
            if (waiting) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(100.dp),
                    )
            }
            else {
            Row {
                Button(
                    onClick = {
                        timeout = false
                        valid = false
                        if (login != null) {
                            scope.launch {
                                waiting = true
                                if (login(username, password, scope).equals("valid")) {
                                    valid = false
                                    waiting = false
                                } else if (login(username, password, scope).equals("timeout")) {
                                    timeout = true
                                    valid = false
                                    waiting = false
                                } else {
                                    valid = true
                                    waiting = false
                                }
                            }
                        }
                    },
                    enabled = enabled,
                ) {
                    Text("Iniciar Sesión")
                }

                Spacer(Modifier.width(16.dp))

                Button(
                    onClick = {
                        register()
                    },
                ) {
                    Text(
                        text = "¿No tienes cuenta? \n Registrate aquí.",
                        textAlign = TextAlign.Center,

                    )
                }

            }

            Spacer(modifier = Modifier.height(18.dp))

            darkMode(
                changeTheme = changeTheme,
                isDarkModeOn = isDarkModeOn,
            )

            }
        }
    }

}