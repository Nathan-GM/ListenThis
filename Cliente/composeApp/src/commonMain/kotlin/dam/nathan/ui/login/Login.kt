package dam.nathan.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dam.nathan.darkMode

@Composable
fun LoginScreen(
    isDarkModeOn: Boolean,
    register: () -> Unit,
    login: ((String, String) -> String)? = null,
    changeTheme: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var valid by remember { mutableStateOf(false) }
    var timeout by remember { mutableStateOf(false) }
    var enabled = username.isNotBlank() && password.isNotBlank()

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            /* TODO make this text visible to Android */
            Text(
                text = "Bienvenido a ListenThis",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
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
                },)

            Spacer(modifier = Modifier.height(16.dp))

            if (valid) {
                Text(text = "Usuario o contraseña incorrectos",
                    color = MaterialTheme.colorScheme.error)
            }

            if (timeout) {
                Text(text = "No se pudo conectar al servidor",
                    color = MaterialTheme.colorScheme.error)
            }

            Row {
                Button(
                    onClick = {
                        if (login != null) {
                            if (login(username, password).equals("valid")) {
                                valid = false
                            } else if (login(username, password).equals("timeout")){
                                timeout = true
                                valid = false
                            } else {
                                valid = true
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