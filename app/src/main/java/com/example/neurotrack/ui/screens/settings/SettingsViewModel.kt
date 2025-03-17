package com.example.neurotrack.ui.screens.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.repository.ConvertApiRepository
import com.example.neurotrack.data.repository.DataExportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import androidx.core.content.FileProvider
import android.os.Environment

data class SettingsState(
    val showShareDialog: Boolean = false,
    val showClearConfirmation: Boolean = false,
    val exportedFilePath: String? = null,
    val convertedPdfPath: String? = null,
    val showConversionSuccessDialog: Boolean = false,
    val showConversionErrorDialog: Boolean = false,
    val conversionError: String? = null
)

class SettingsViewModel(
    private val convertApiRepository: ConvertApiRepository,
    private val dataExportRepository: DataExportRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    fun toggleDarkMode() {
        _darkMode.value = !_darkMode.value
    }

    fun toggleNotifications() {
        _notificationsEnabled.value = !_notificationsEnabled.value
    }

    fun exportDataToCsv(context: Context) {
        viewModelScope.launch {
            try {
                // Usar o DataExportRepository existente para exportar dados
                val result = dataExportRepository.exportDataToCSV()
                
                result.fold(
                    onSuccess = { file ->
                        // Copiar o arquivo para o diretório de downloads
                        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        if (!downloadsDir.exists()) {
                            downloadsDir.mkdirs()
                        }
                        
                        val fileName = "neurotracker_data_${System.currentTimeMillis()}.csv"
                        val destinationFile = File(downloadsDir, fileName)
                        
                        file.copyTo(destinationFile, overwrite = true)
                        
                        // Atualizar o estado com o caminho do arquivo exportado
                        _state.value = _state.value.copy(
                            exportedFilePath = destinationFile.absolutePath,
                            showShareDialog = true
                        )
                    },
                    onFailure = { error ->
                        // Em caso de erro, atualizar o estado
                        _state.value = _state.value.copy(
                            conversionError = "Erro ao exportar dados: ${error.message}",
                            showConversionErrorDialog = true
                        )
                    }
                )
            } catch (e: Exception) {
                // Em caso de erro, atualizar o estado
                _state.value = _state.value.copy(
                    conversionError = "Erro ao exportar dados: ${e.message}",
                    showConversionErrorDialog = true
                )
            }
        }
    }
    
    fun shareExportedFile(context: Context) {
        state.value.exportedFilePath?.let { filePath ->
            val file = File(filePath)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            context.startActivity(Intent.createChooser(intent, "Compartilhar arquivo CSV"))
        }
    }
    
    fun dismissShareDialog() {
        _state.value = _state.value.copy(showShareDialog = false)
    }
    
    fun dismissErrorDialog() {
        _state.value = _state.value.copy(showConversionErrorDialog = false)
    }

    fun convertCsvToPdf(context: Context) {
        viewModelScope.launch {
            val result = convertApiRepository.convertCsvToPdf(context)
            
            result.onSuccess { pdfFile ->
                _state.value = _state.value.copy(
                    convertedPdfPath = pdfFile.absolutePath,
                    showConversionSuccessDialog = true
                )
            }.onFailure { error ->
                _state.value = _state.value.copy(
                    conversionError = error.message,
                    showConversionErrorDialog = true
                )
            }
        }
    }
    
    fun createSaveFileIntent(): Intent {
        return convertApiRepository.createSaveFileIntent("neurotrack_data_${System.currentTimeMillis()}.pdf")
    }
    
    fun saveFileToUri(context: Context, pdfFile: File, uri: Uri): Boolean {
        return convertApiRepository.saveFileToUri(context, pdfFile, uri)
    }
    
    fun sharePdfFile(context: Context) {
        _state.value.convertedPdfPath?.let { path ->
            val pdfFile = File(path)
            if (pdfFile.exists()) {
                convertApiRepository.sharePdfFile(context, pdfFile)
            }
        }
    }
    
    fun openPdfFile(context: Context) {
        _state.value.convertedPdfPath?.let { path ->
            val pdfFile = File(path)
            if (pdfFile.exists()) {
                convertApiRepository.openPdfFile(context, pdfFile)
            }
        }
    }
    
    fun dismissConversionSuccessDialog() {
        _state.value = _state.value.copy(showConversionSuccessDialog = false)
    }
    
    fun requestStoragePermissions(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    STORAGE_PERMISSION_CODE
                )
            }
        }
    }

    fun hasStoragePermissions(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun dismissClearConfirmation() {
        _state.value = _state.value.copy(showClearConfirmation = false)
    }

    companion object {
        const val STORAGE_PERMISSION_CODE = 101
    }
} 