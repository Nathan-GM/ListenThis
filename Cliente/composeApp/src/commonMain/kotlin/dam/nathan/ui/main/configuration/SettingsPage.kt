package dam.nathan.ui.main.configuration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass
import dam.nathan.darkMode
import dam.nathan.ib64
import dam.nathan.models.User
import dam.nathan.models.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    userVM: UserViewModel,
    isDarkModeOn: Boolean,
    changeTheme: () -> Unit,
    goToLogin: () -> Unit,
) {

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val scope = rememberCoroutineScope()
    var waiting by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf(false) }

    var topPadding = 0

    if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
        topPadding = 72
    } else {
        topPadding = 120
    }

    var openDialog by remember { mutableStateOf(false) }

    var openEditDialog by remember { mutableStateOf(false) }
    var newUsername by remember { mutableStateOf("") }
    var newAvatar by remember { mutableStateOf("") }
    var biography by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configuración de ${userVM.user.value.user?.username ?: "noUser"}",
                        fontSize = 25.sp
                    )
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (waiting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
                )
            }

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
                                text = "Eliminar cuenta de ${userVM.user.value.user?.username ?: "noUser"}",
                                style = MaterialTheme.typography.displaySmall
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "¿Estás seguro de que deseas eliminar la cuenta?" +
                                        "\n\nTodo lo relacionado con esta cuenta (fotos de avatar y publicaciones) se eliminará"
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                TextButton(
                                    onClick = { openDialog = false },
                                ) {
                                    Text("Cancelar")
                                }
                                Spacer(modifier = Modifier.width(15.dp))
                                TextButton(
                                    onClick = {
                                        waiting = true
                                        scope.launch {
                                            val result = userVM.deleteAccount()
                                            if (result == "ok") {
                                                goToLogin()
                                            } else if (result == "error") {
                                                error = true
                                            }
                                        }
                                    },
                                    colors = ButtonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                        disabledContentColor = MaterialTheme.colorScheme.onErrorContainer,
                                        disabledContainerColor = MaterialTheme.colorScheme.errorContainer
                                    )
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }

            if (openEditDialog) {
                biography = userVM.user.value.user?.biography ?: ""
                BasicAlertDialog(
                    onDismissRequest = {

                    }
                ) {
                    Surface(
                        modifier = Modifier.wrapContentSize(),
                        tonalElevation = AlertDialogDefaults.TonalElevation
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Modificando ${userVM.user.value.user?.username ?: "noUser"}",
                                style = MaterialTheme.typography.displaySmall
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            OutlinedTextField(
                                value = newUsername,
                                onValueChange = { newUsername = it },
                                label = { Text("Nuevo nombre de usuario") },
                                singleLine = true,
                                placeholder = {
                                    Text(
                                        userVM.user.value.user?.username
                                            ?: "Introduce tu nuevo usuario"
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = biography,
                                onValueChange = { biography = it },
                                label = { Text("Nueva biografia") },
                                singleLine = true,
                                placeholder = {
                                    Text(
                                        userVM.user.value.user?.biography
                                            ?: "Introduce tu nueva biografia"
                                    )
                                },
                            )

                            Text(if (userVM.user.value.user?.avatar != null) "Actualizar foto de perfil" else "Agregar foto de perfil")

                            ib64(
                                onChange = {
                                    newAvatar = it
                                }
                            )

                            Row(
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                TextButton(
                                    onClick = { openEditDialog = false },
                                ) {
                                    Text("Cancelar")
                                }
                                Spacer(modifier = Modifier.width(15.dp))
                                TextButton(
                                    onClick = {
                                        println(userVM.user.value.user!!.id)
                                        waiting = true
                                        scope.launch {
                                            val newData = User(
                                                username = newUsername,
                                                password = userVM.user.value.user!!.password,
                                                avatar = if (newAvatar.isNotEmpty() && newAvatar != "") newAvatar else userVM.user.value.user!!.avatar,
                                                biography = biography,
                                                followedGenres = userVM.user.value.user!!.followedGenres,
                                            )
                                            val result = userVM.editAccount(newData)
                                            if (result == "error") {
                                                error = true
                                                waiting = false
                                                openEditDialog = false
                                            } else {
                                                println("SP: newUSER: ${userVM.user.value.user!!.id}")
                                                waiting = false

                                                newUsername = ""
                                                biography = ""
                                                newAvatar = ""

                                                openEditDialog = false
                                            }
                                        }
                                    },
                                    enabled = newUsername != null && newUsername.isNotEmpty()
                                ) {
                                    Text("Realizar cambios")
                                }

                            }
                        }
                    }
                }
            }

            Card(
                Modifier.wrapContentHeight().fillMaxWidth().padding(top = topPadding.dp)
                    .consumeWindowInsets(
                        PaddingValues(top = topPadding.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "Configuración de ${userVM.user.value.user?.username ?: "noUser"}",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Datos actuales del usuario:")
                    Column(
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Text("Nombre de usuario: ${userVM.user.value.user?.username ?: "noUser"}")
                        Text("Biografia del usuario: ${userVM.user.value.user?.biography ?: "No cuenta con ninguna biografia"}")
                        Text("¿Cuenta con un avatar?: ${if (userVM.user.value.user?.avatar != null && userVM.user.value.user?.avatar != "") "Si" else "No"}")
                    }
                    Row {
                        Button(
                            onClick = {
                                openEditDialog = true
                            }
                        ) {
                            Text("Modificar datos")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                openDialog = true
                            },
                            colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                disabledContentColor = MaterialTheme.colorScheme.onErrorContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text("Eliminar cuenta")
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    if (error) {
                        Text("Ha ocurrido un problema. Por favor, intentalo de nuevo más tarde.")
                    }

                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
                Card(
                    modifier = Modifier.wrapContentHeight().fillMaxWidth()
                ) {
                    Column {
                        Text(text = "Cambiar a modo " + (if (isDarkModeOn) "dia" else "noche"))
                        Spacer(modifier = Modifier.height(10.dp))
                        darkMode(isDarkModeOn = isDarkModeOn, changeTheme = changeTheme)
                    }
                }
            }
        }
    }


}