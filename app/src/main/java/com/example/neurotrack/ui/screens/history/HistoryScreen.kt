package com.example.neurotrack.ui.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.example.neurotrack.ui.components.RecordsList
import com.example.neurotrack.ui.screens.settings.SettingsViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = koinViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    val settingsViewModel: SettingsViewModel = koinViewModel()
    val settingsState by settingsViewModel.state.collectAsState()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Registros") },
                actions = {
                    IconButton(
                        onClick = {
                            settingsViewModel.exportDataToCsv(context)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Exportar CSV"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            RecordsList(
                records = state.records,
                onRecordClick = { /* não faz nada */ },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }

    if (settingsState.showShareDialog) {
        AlertDialog(
            onDismissRequest = {
                settingsViewModel.dismissShareDialog()
            },
            title = { Text("Exportação concluída") },
            text = { Text("Os dados foram exportados com sucesso. Deseja compartilhar o arquivo agora?") },
            confirmButton = {
                TextButton(onClick = {
                    settingsViewModel.shareExportedFile(context)
                    settingsViewModel.dismissShareDialog()
                }) {
                    Text("Compartilhar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    settingsViewModel.dismissShareDialog()
                }) {
                    Text("Fechar")
                }
            }
        )
    }

    if (settingsState.showConversionErrorDialog) {
        AlertDialog(
            onDismissRequest = { settingsViewModel.dismissErrorDialog() },
            title = { Text("Erro na exportação") },
            text = {
                Text(settingsState.conversionError ?: "Ocorreu um erro ao exportar os dados.")
            },
            confirmButton = {
                TextButton(onClick = { settingsViewModel.dismissErrorDialog() }) {
                    Text("OK")
                }
            }
        )
    }
}
