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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

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
    private val convertApiRepository: ConvertApiRepository
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

    fun exportData() {
        _state.value = _state.value.copy(
            showShareDialog = true,
            exportedFilePath = "/storage/emulated/0/Download/neurotrack_data.csv"
        )
    }

    fun shareCSVFile() {
        // Simulação de compartilhamento
        dismissShareDialog()
    }

    fun dismissShareDialog() {
        _state.value = _state.value.copy(showShareDialog = false)
    }

    fun clearData() {
        // Simulação de limpeza de dados
        _state.value = _state.value.copy(showClearConfirmation = true)
    }

    fun dismissClearConfirmation() {
        _state.value = _state.value.copy(showClearConfirmation = false)
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
    
    fun dismissConversionErrorDialog() {
        _state.value = _state.value.copy(showConversionErrorDialog = false)
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

    companion object {
        const val STORAGE_PERMISSION_CODE = 101
    }
} 