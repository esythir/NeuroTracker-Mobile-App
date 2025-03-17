package com.example.neurotrack.ui.screens.settings

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    userName: String = "João Silva",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val darkMode by viewModel.darkMode.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    var dataExportDialogVisible by remember { mutableStateOf(false) }
    var aboutDialogVisible by remember { mutableStateOf(false) }
    
    // Launcher para salvar o arquivo PDF
    val savePdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                state.convertedPdfPath?.let { path ->
                    val pdfFile = File(path)
                    val success = viewModel.saveFileToUri(context, pdfFile, uri)
                    if (success) {
                        Toast.makeText(context, "Arquivo salvo com sucesso!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Erro ao salvar arquivo", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Perfil do usuário
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = userName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Editar perfil",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
            
            // Seção de Aparência
            item {
                Text(
                    text = "Aparência",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = "Modo Escuro"
                        )
                        
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        ) {
                            Text(
                                text = "Modo escuro",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Ativar tema escuro no aplicativo",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        Switch(
                            checked = darkMode,
                            onCheckedChange = { viewModel.toggleDarkMode() }
                        )
                    }
                }
            }
            
            item { Divider() }
            
            // Seção de Notificações
            item {
                Text(
                    text = "Notificações",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            
            item {
                SettingsSwitchItem(
                    icon = Icons.Default.Notifications,
                    title = "Lembretes diários",
                    description = "Receber lembretes para registrar comportamentos",
                    checked = notificationsEnabled,
                    onCheckedChange = { viewModel.toggleNotifications() }
                )
            }
            
            item { Divider() }
            
            // Seção de Dados
            item {
                Text(
                    text = "Dados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            
            item {
                SettingsClickableItem(
                    icon = Icons.Default.CloudDownload,
                    title = "Exportar dados",
                    description = "Exportar registros para arquivo CSV",
                    onClick = { dataExportDialogVisible = true }
                )
            }
            
            item {
                SettingsClickableItem(
                    icon = Icons.Default.Delete,
                    title = "Limpar dados",
                    description = "Apagar todos os registros",
                    onClick = { /* Mostrar diálogo de confirmação */ }
                )
            }
            
            item { Divider() }
            
            // Seção de Sobre
            item {
                Text(
                    text = "Sobre",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            
            item {
                SettingsClickableItem(
                    icon = Icons.Default.Info,
                    title = "Sobre o NeuroTracker",
                    description = "Versão 1.0.0",
                    onClick = { aboutDialogVisible = true }
                )
            }
            
            item {
                SettingsClickableItem(
                    icon = Icons.Default.Email,
                    title = "Contato",
                    description = "Enviar feedback ou relatar problemas",
                    onClick = { /* Abrir email */ }
                )
            }
        }
    }
    
    // Diálogo de exportação de dados
    if (dataExportDialogVisible) {
        AlertDialog(
            onDismissRequest = { dataExportDialogVisible = false },
            title = { Text("Exportar dados") },
            text = { Text("Seus dados serão exportados para um arquivo CSV que você poderá compartilhar.") },
            confirmButton = {
                TextButton(onClick = {
                    // Lógica para exportar dados
                    dataExportDialogVisible = false
                }) {
                    Text("Exportar")
                }
            },
            dismissButton = {
                TextButton(onClick = { dataExportDialogVisible = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Diálogo sobre o app
    if (aboutDialogVisible) {
        AlertDialog(
            onDismissRequest = { aboutDialogVisible = false },
            title = { Text("Sobre o NeuroTracker") },
            text = {
                Column {
                    Text("NeuroTracker v1.0.0")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Um aplicativo para rastreamento e análise de comportamentos neurodivergentes.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("© 2023 NeuroTracker")
                }
            },
            confirmButton = {
                TextButton(onClick = { aboutDialogVisible = false }) {
                    Text("Fechar")
                }
            }
        )
    }
    
    // Diálogo de compartilhamento
    if (state.showShareDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissShareDialog() },
            title = { Text("Exportação concluída") },
            text = { Text("Seus dados foram exportados com sucesso. Deseja compartilhar o arquivo CSV?") },
            confirmButton = {
                Button(onClick = { viewModel.shareCSVFile() }) {
                    Text("Compartilhar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissShareDialog() }) {
                    Text("Fechar")
                }
            }
        )
    }
    
    // Diálogo de confirmação de limpeza
    if (state.showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissClearConfirmation() },
            title = { Text("Dados limpos") },
            text = { Text("Todos os seus registros foram apagados com sucesso.") },
            confirmButton = {
                Button(onClick = { viewModel.dismissClearConfirmation() }) {
                    Text("OK")
                }
            }
        )
    }
    
    // No diálogo de sucesso da conversão, adicione um botão para abrir o PDF
    if (state.showConversionSuccessDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissConversionSuccessDialog() },
            title = { Text("Conversão Concluída") },
            text = { 
                Text("O arquivo foi convertido com sucesso. O que deseja fazer?") 
            },
            confirmButton = {
                Column {
                    Button(
                        onClick = {
                            // Iniciar intent para salvar o arquivo
                            val intent = viewModel.createSaveFileIntent()
                            savePdfLauncher.launch(intent)
                        },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Text("Salvar em...")
                    }
                    
                    Button(
                        onClick = {
                            viewModel.sharePdfFile(context)
                        },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Text("Compartilhar")
                    }
                    
                    Button(
                        onClick = {
                            viewModel.openPdfFile(context)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Visualizar PDF")
                    }
                }
            },
            dismissButton = {
                Button(onClick = { viewModel.dismissConversionSuccessDialog() }) {
                    Text("Fechar")
                }
            }
        )
    }

    if (state.showConversionErrorDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissConversionErrorDialog() },
            title = { Text("Erro na Conversão") },
            text = { 
                Text("Ocorreu um erro durante a conversão:\n${state.conversionError}") 
            },
            confirmButton = {
                Button(onClick = { viewModel.dismissConversionErrorDialog() }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsClickableItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
} 