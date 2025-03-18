package com.example.neurotrack.data.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.example.neurotrack.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class ConvertApiRepository {
    private val apiService = RetrofitClient.convertApiService
    private val TAG = "ConvertApiRepository"
    
    private val apiSecret = "secret_JMgdqX4Os3xG2lj7"
    
    suspend fun convertCsvToPdf(context: Context): Result<File> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Iniciando conversão de CSV para PDF")
            
            // Criar um arquivo CSV de teste
            val testCsvFile = createTestCsvFile(context)
            Log.d(TAG, "Arquivo CSV de teste criado: ${testCsvFile.absolutePath}")
            
            val requestFile = testCsvFile.asRequestBody("text/csv".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", testCsvFile.name, requestFile)
            
            Log.d(TAG, "Enviando requisição para ConvertAPI")
            val response = apiService.convertFile(
                apiSecret = apiSecret,
                apiKey = "",
                fromFormat = "csv",
                toFormat = "pdf",
                file = body
            )
            
            if (response.isSuccessful) {
                Log.d(TAG, "Conversão bem-sucedida, salvando arquivo PDF")
                val outputFile = File(context.cacheDir, "neurotrack_data.pdf")
                
                response.body()?.let { responseBody ->
                    FileOutputStream(outputFile).use { outputStream ->
                        responseBody.byteStream().use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                    
                    Log.d(TAG, "Arquivo PDF salvo em cache: ${outputFile.absolutePath}")
                    return@withContext Result.success(outputFile)
                } ?: run {
                    Log.e(TAG, "Resposta vazia da API")
                    return@withContext Result.failure(Exception("Resposta vazia"))
                }
            } else {
                Log.e(TAG, "Falha na conversão: ${response.code()} ${response.message()}")
                return@withContext Result.failure(Exception("Falha na conversão: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro durante a conversão", e)
            return@withContext Result.failure(e)
        }
    }
    
    private fun createTestCsvFile(context: Context): File {
        val testFile = File(context.cacheDir, "test_data.csv")
        FileOutputStream(testFile).use { out ->
            out.write("Nome,Idade,Email\nJoão,30,joao@example.com\nMaria,25,maria@example.com".toByteArray())
        }
        return testFile
    }
    
    fun createSaveFileIntent(fileName: String): Intent {
        return Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, fileName)
        }
    }
    
    fun saveFileToUri(context: Context, sourceFile: File, destinationUri: Uri): Boolean {
        return try {
            context.contentResolver.openOutputStream(destinationUri)?.use { outputStream ->
                sourceFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao salvar arquivo para URI", e)
            false
        }
    }
    
    fun sharePdfFile(context: Context, pdfFile: File) {
        try {
            Log.d(TAG, "Compartilhando arquivo: ${pdfFile.absolutePath}")
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )
            
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            context.startActivity(Intent.createChooser(intent, "Compartilhar PDF"))
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao compartilhar arquivo", e)
        }
    }
    
    fun openPdfFile(context: Context, pdfFile: File) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                pdfFile
            )
            
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao abrir PDF", e)
        }
    }
} 