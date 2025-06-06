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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import dam.nathan.models.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(userVM: UserViewModel, isDarkModeOn: Boolean, changeTheme: () -> Unit) {

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
            if (openDialog) {
                BasicAlertDialog(
                    onDismissRequest = {
                        openDialog = false
                    },
                ) {
                    Surface(
                        modifier = Modifier.wrapContentSize(),
                        tonalElevation = AlertDialogDefaults.TonalElevation
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Eliminar cuenta de ${userVM.user.value.user?.username ?: "noUser"}", style = MaterialTheme.typography.displaySmall
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "¿Estás seguro de que deseas eliminar la cuenta?" +
                                        "\n\nTodo lo relacionado con esta cuenta (fotos de avatar y publicaciones) se eliminará"
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.align(Alignment.End)
                            ){
                                TextButton(
                                    onClick = { openDialog = false },
                                ) {
                                    Text("Cancelar")
                                }
                                Spacer(modifier = Modifier.width(15.dp))
                                TextButton(
                                    onClick = { openDialog = false },
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
                        "Configuración de ${userVM.user.value.user!!.username}",
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
                                println(userVM.user.value.user!!)
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