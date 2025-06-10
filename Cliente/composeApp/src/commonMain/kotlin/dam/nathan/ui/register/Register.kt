package dam.nathan.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dam.nathan.ib64
import dam.nathan.models.User
import dam.nathan.models.repositories.UserRepository
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    goLogin: () -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmationPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmationPassword by remember { mutableStateOf(false) }
    var avatar = remember { mutableStateOf("") }
    var conflict by remember { mutableStateOf(false) }
    var to by remember { mutableStateOf(false) }
    var enabled =
        username.isNotBlank() &&
                password.isNotBlank() &&
                confirmationPassword.isNotBlank() &&
                (password.equals(confirmationPassword)) &&
                password.length > 8 &&
                (password.contains("[A-Za-z]".toRegex()) //letters, uppercase & lowercase
                        && (password.contains("[0-9]".toRegex()) // numbers
                        || password.contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex()) //special character
                        )
                        )

    var openDialog by remember { mutableStateOf(false) }

    var waiting by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var userRepository = UserRepository()


    if (openDialog) {
        BasicAlertDialog(
            onDismissRequest = {
            },
        ) {
            Surface(
                modifier = Modifier.wrapContentSize(),
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Datos sobre la contraseña",
                        style = MaterialTheme.typography.displaySmall
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "La contraseña ha de tener: \n" +
                                "- Mínimo 1 letra \n" +
                                "- Mínimo 1 número o carácter especial \n" +
                                "- Ha de tener 8 carácteres",
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    TextButton(
                        onClick = { openDialog = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Entendido")
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Registrarse en ListenThis",
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineLarge
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                placeholder = { Text("Introduce el nombre de usuario") },
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { showPassword = !showPassword }
                        ) {
                            val icon =
                                if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            Icon(
                                icon,
                                contentDescription = if (showPassword) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        openDialog = true
                    }
                ) {
                    Icon(
                        Icons.Filled.Info,
                        "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmationPassword,
                onValueChange = { confirmationPassword = it },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = if (showConfirmationPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            showConfirmationPassword = !showConfirmationPassword
                        }
                    ) {
                        val icon =
                            if (showConfirmationPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        Icon(
                            icon,
                            contentDescription = if (showConfirmationPassword) "Ocultar confirmación de contraseña" else "Mostrar confirmación de contraseña"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ib64(
                onChange = {
                    avatar.value = it
                }
            )

            if (conflict) {
                if (to) {
                    Text(
                        text = "No se puede conectar al servidor.",
                        color = MaterialTheme.colorScheme.error,
                    )
                } else {
                    Text(
                        text = "El usuario $username se encuentra en uso. Selecciona otro.",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            if (waiting) {
                CircularProgressIndicator(
                    modifier = Modifier.width(100.dp)
                )
            } else {
                Row {
                    Button(
                        onClick = {
                            waiting = true
                            scope.launch {
                                var u = User(
                                    username = username,
                                    password = password,
                                    avatar = avatar.value
                                )
                                val result = userRepository.register(u)
                                System.err.println(result)
                                if (result == HttpStatusCode.Conflict) {
                                    conflict = true
                                    to = false
                                    waiting = false
                                } else if (result == HttpStatusCode.Created) {
                                    conflict = false
                                    to = false
                                    waiting = false
                                    goLogin()
                                } else if (result == HttpStatusCode.NotFound) {
                                    conflict = true
                                    to = true
                                    waiting = false
                                } else {
                                    conflict = true
                                    to = true
                                    waiting = false
                                }
                            }
                        },
                        enabled = enabled,
                    ) {
                        Text("Registrarse")
                    }

                    Spacer(Modifier.width(16.dp))

                    Button(
                        onClick = {
                            goLogin()
                        },
                    ) {
                        Text("Volver")
                    }
                }

            }
        }
    }
}
